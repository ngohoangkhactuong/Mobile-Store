package com.seo.mobilestore.service;

import com.seo.mobilestore.data.dto.ChangePasswordByOTPDTO;
import com.seo.mobilestore.data.dto.ChangePasswordDTO;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.user.UserCreationDTO;
import com.seo.mobilestore.data.dto.user.UserDTO;
import com.seo.mobilestore.data.dto.user.UserProfileDTO;
import com.seo.mobilestore.data.entity.Status;
import com.seo.mobilestore.data.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDTO findById(long id);

    void activeUser(String token);

    void updateTokenForgetPassword(String email, String token);

    String checkStatusUser(Status status);

    boolean changePasswordByToken(ChangePasswordDTO changePasswordDTO);

    boolean checkValidOldPassword(String oldPass, String newPass);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    void changePasswordByOTP(ChangePasswordByOTPDTO changePasswordByOTPDTO);

    void updateTokenActive(String email, String token);

    Long getCurrentUserId();

    UserDTO create(UserCreationDTO userCreationDTO);

    UserDTO update(long id, UserProfileDTO userProfileDTO);

    PaginationDTO findAllPagination(int no, int limit);

    PaginationDTO findAllByKeywordsPagination(String keyword, int no, int limit);

    UserDTO showUserDetail();

    UserDTO disable(long id);

    UserDTO unlock(long id);
}
