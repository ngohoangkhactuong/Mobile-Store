package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.common.enumeration.ERole;
import com.seo.mobilestore.common.util.Validation;
import com.seo.mobilestore.data.dto.ChangePasswordByOTPDTO;
import com.seo.mobilestore.data.dto.ChangePasswordDTO;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.user.UserCreationDTO;
import com.seo.mobilestore.data.dto.user.UserDTO;
import com.seo.mobilestore.data.dto.user.UserProfileDTO;
import com.seo.mobilestore.data.entity.Status;
import com.seo.mobilestore.data.entity.User;
import com.seo.mobilestore.data.mapper.RoleMapper;
import com.seo.mobilestore.data.mapper.UserMapper;
import com.seo.mobilestore.data.repository.RoleRepository;
import com.seo.mobilestore.data.repository.StatusRepository;
import com.seo.mobilestore.data.repository.UserRepository;
import com.seo.mobilestore.exception.*;
import com.seo.mobilestore.service.FileService;
import com.seo.mobilestore.service.RoleService;
import com.seo.mobilestore.service.StatusService;
import com.seo.mobilestore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("userDetailsService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private Validation validation;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AuditorAware auditorAware;

    /**
     * Method get current user by id when login success
     */
    @Override
    public Long getCurrentUserId() {
        return (Long) auditorAware.getCurrentAuditor().orElse(null);
    }

    /**
     * Mehod find user by email
     */
    @Override
    public User findByEmail(String email) {
        if (email.isEmpty()) {
            throw new ValidationException(Collections.singletonMap("User email ", email));
        }
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new InternalServerErrorException(Collections.singletonMap(email, "Not found")));
    }

    /**
     * Method update otp of user
     */
    @Override
    public void updateTokenActive(String email, String otp) {
        Map<String, Object> errors = new HashMap<>();
        if (email.isEmpty())
            errors.put("email ", email);
        else if (otp.isEmpty())
            errors.put("otp ", otp);
        else if (!errors.isEmpty())
            throw new ResourceNotFoundException(errors);
        User user = this.findByEmail(email);
        user.setOtp(otp);

        userRepository.save(user);
    }

    /**
     * Method load user by name for authentication
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (email.isEmpty()) {
            throw new ValidationException(Collections.singletonMap("User email ", email));
        }
        return UserDetailsImpl.build(findByEmail(email));
    }

    @Override
    public UserDTO findById(long id) {
        return this.userMapper.toDTO(this.userRepository.findById(id).orElseThrow(() ->
                new InternalServerErrorException(Collections.singletonMap(id, "Not found"))));
    }

    /**
     * Method active user if otp true
     */
    @Override
    public void activeUser(String Otp) {
        if (Otp.isEmpty())
            throw new ValidationException(Collections.singletonMap("User Otp ", Otp));

        User user = this.userRepository.findByOtp(Otp).orElseThrow(() ->
                new InternalServerErrorException(Collections.singletonMap(Otp, "Not found")));

        user.setStatus(true);
        user.setOtp("");

        this.userRepository.save(user);
    }

    /**
     * Method check exists email
     */
    @Override
    public boolean existsByEmail(String email) {
        if (email.isEmpty())
            throw new ValidationException(Collections.singletonMap("User email ", email));

        return false;
    }

    /**
     * Method update password by otp if user forgot password
     */
    @Override
    public void updateTokenForgetPassword(String email, String otp) {
        Map<String, Object> errors = new HashMap<>();
        if (email.isEmpty())
            errors.put("email ", email);
        else if (otp.isEmpty())
            errors.put("otp ", otp);
        else if (!errors.isEmpty())
            throw new ResourceNotFoundException(errors);

        User user = this.findByEmail(email);
        user.setOtp(otp);

        userRepository.save(user);
    }

    /**
     * Method check valid old password for change password
     */
    @Override
    public boolean checkValidOldPassword(String oldPass, String confirmPass) {
        if (confirmPass.isEmpty()) {
            throw new ValidationException(Collections.singletonMap("confirm password ", confirmPass));
        }
        return passwordEncoder.matches(confirmPass, oldPass);
    }

    /**
     * Method create user
     */
    @Override
    public UserDTO create(UserCreationDTO userCreationDTO) {
        if (Objects.isNull(userCreationDTO)) {
            throw new ResourceNotFoundException(Collections.singletonMap("Creation user ", userCreationDTO));
        }
        // check existing user info
        Map<String, Object> errors = new HashMap<String, Object>();
        if (userRepository.existsByEmail(userCreationDTO.getEmail())) {
            errors.put("email", userCreationDTO.getEmail());
        }
        if (errors.size() > 0) {
            throw new ConflictException(Collections.singletonMap("userCreationDTO", errors));
        }

        // set info for user
        User user = userMapper.toEntity(userCreationDTO);

        // set default role and status will be set default
        // base on auth provider register
        ERole defaultRole;
        boolean defaultStatus = false;
        boolean defaultLockStatus = false;


        user.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));
        user.setRole(
                roleRepository.findByName(ERole.Customer.toString())
                        .orElseThrow(() -> new InternalServerErrorException(
                                Collections.singletonMap("Role", ERole.Customer.toString()))));

        user.setStatus(true);
        user.setLock_status(defaultLockStatus);

        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    /**
     * Method update profile user
     */
    @Override
    public UserDTO update(long id, UserProfileDTO userProfileDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthen",
                        null, null)));

        // if user is not admin
        if(user.getRole().getId() != 1){
            //Authentication user want update profile
            if (user.getId() != id)
                throw new AccessDeniedException();
        }

        User usr = this.userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("User ID ", id)));

        //Check null userProfileDTO input
        if (Objects.isNull(userProfileDTO))
            throw new ValidationException(Collections.singletonMap("user profile dto ", userProfileDTO));

        usr.setEmail(userProfileDTO.getEmail());
        usr.setFullName(userProfileDTO.getFullName());
        usr.setGender(userProfileDTO.getGender());
        usr.setBirthDay(userProfileDTO.getBirthDay());
        usr.setAddressList(null);

        return userMapper.toDTO(userRepository.save(usr));
    }

    /**
     * Method find all user
     */
    @Override
    public PaginationDTO findAllPagination(int no, int limit) {
        Page<UserDTO> page = this.userRepository.findByStatusIsTrue(PageRequest.of(no, limit)).map(item -> userMapper.toDTO(item));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(), page.getSize(),
                page.getNumber());
    }

    /**
     * Method Find user by name
     */
    @Override
    public PaginationDTO findAllByKeywordsPagination(String keyword, int no, int limit) {

        Page<User> userPage = userRepository.findAllByFullNameContainingOrEmailContaining(keyword, keyword, PageRequest.of(no, limit));

        List<UserDTO> users = userPage.getContent().stream().map(user -> userMapper.toDTO(user))
                .collect(Collectors.toList());

        return new PaginationDTO(users, userPage.isFirst(), userPage.isLast(), userPage.getTotalPages(),
                userPage.getTotalElements(), userPage.getSize(), userPage.getNumberOfElements());
    }


    /**
     * Method show user detail
     */
    @Override
    public UserDTO showUserDetail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = this.userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("email", email)));

        return userMapper.toDTO(user);
    }

    /**
     * lock user
     */
    @Override
    public UserDTO disable(long id) {
        User user = this.userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("User ID ", id)));

        user.setLock_status(true);
        user.setAddressList(null);

        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO unlock(long id){
        User usr = this.userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(Collections.singletonMap("User ID ", id)));

        usr.setLock_status(false);
        usr.setAddressList(null);

        userRepository.save(usr);

        return userMapper.toDTO(usr);
    }

    /**
     * Method change password user (pre login successful)
     */
    @Override
    public boolean changePasswordByToken(ChangePasswordDTO changePasswordDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InternalServerErrorException(messageSource.getMessage("error.userAuthenr",
                        null, null)));

        String oldPassword = user.getPassword();
        String newPassword = changePasswordDTO.getNewPassword();

        // Check newPassword is similar to oldPassword
        if (checkValidOldPassword(oldPassword, newPassword))
            throw new InternalServerErrorException(messageSource.getMessage("error.passwordNotSimilar",
                    null, null));

        // Check confirm old password similar to old password
        if (checkValidOldPassword(oldPassword, changePasswordDTO.getOldPassword())) {

            user.setPassword(passwordEncoder.encode(newPassword));
            user.setAddressList(null);

            userRepository.save(user);

        } else {
            throw new InvalidExcelVersionException(
                    Collections.singletonMap(oldPassword, messageSource.getMessage("error.passwordIncorrect",
                            null, null)));
        }

        return true;
    }

    /**
     * Method change password by otp if user forgot password
     */
    @Override
    public void changePasswordByOTP(ChangePasswordByOTPDTO changePasswordByOTPDTO) {
        if (Objects.isNull(changePasswordByOTPDTO)) {
            throw new ResourceNotFoundException(Collections.singletonMap("Password ", changePasswordByOTPDTO));
        }
        User user = this.userRepository.findByOtp(changePasswordByOTPDTO.getOtp()).orElseThrow(
                () -> new InternalServerErrorException(
                        this.messageSource.getMessage("error.otp", null, null)));

        if (!this.validation.passwordValid(changePasswordByOTPDTO.getNewPassword())) // Check Password is strong
            throw new InternalServerErrorException(
                    messageSource.getMessage("error.passwordRegex", null, null));
        user.setPassword(new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 10)
                .encode(changePasswordByOTPDTO.getNewPassword()));
        user.setOtp("");
        this.userRepository.save(user);
    }


    /**
     * Method check status user
     *
     * @param status
     * @return message
     */
    @Override
    public String checkStatusUser(Status status) {
        if (Objects.isNull(status)) {
            throw new ResourceNotFoundException(Collections.singletonMap("status ", status));
        }
        String message = "";
        switch (status.getId()) {
            case 1:
                message = "";
                break;
            case 2:
                message = messageSource.getMessage("error.notAvailable", null, null);
                break;
            case 3:
                message = messageSource.getMessage("error.lockUser", null, null);
                break;
        }
        return message;
    }


}