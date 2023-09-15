package com.seo.mobilestore.controller.user;

import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.constant.PageDefault;
import com.seo.mobilestore.data.dto.ChangePasswordByOTPDTO;
import com.seo.mobilestore.data.dto.ChangePasswordDTO;
import com.seo.mobilestore.data.dto.user.UserCreationDTO;
import com.seo.mobilestore.data.dto.user.UserProfileDTO;
import com.seo.mobilestore.service.MailService;
import com.seo.mobilestore.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiURL.USER)
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MailService mailService;

    /**
     * Method find user by id
     *
     * @param id
     * @return show UserDTO if had found id user in data
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        return ResponseEntity.ok(this.userService.findById(id));
    }

    /**
     * Method Create user
     *
     * @param userCreationDTO
     * @return Returns an "ok" response if the address update is successful
     */
    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody UserCreationDTO userCreationDTO) {
        return new ResponseEntity<>(this.userService.create(userCreationDTO), HttpStatus.CREATED);
    }

    /**
     * Method update profile user
     *
     * @param id
     * @param userProfileDTO
     * @return
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @RequestBody UserProfileDTO userProfileDTO) {

        return ResponseEntity.ok(userService.update(id, userProfileDTO));
    }

    /**
     * Method active user by otp
     *
     * @param OTP
     * @return Returns an "ok" response if the address update is successful
     */
    @GetMapping("/active-otp")
    public ResponseEntity<?> activeUserByOTP(@RequestParam(name = "activeOTP") String OTP) {


        this.userService.activeUser(OTP);
        return ResponseEntity.ok(HttpServletResponse.SC_OK);
    }


    /**
     * Method change password by otp if user forgot password
     *
     * @param changePasswordByOTPDTO
     * @return Returns an "ok" response if the address update is successful
     */
    @PostMapping("/change-password-by-otp") // forgot password
    public ResponseEntity<?> changePasswordByOTP(
            @Valid @RequestBody ChangePasswordByOTPDTO changePasswordByOTPDTO) {
        this.userService.changePasswordByOTP(changePasswordByOTPDTO);

        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_OK,
                messageSource.getMessage("success.passwordChange", null, null), null));
    }


    /**
     * Method change password user (pre login successful)
     *
     * @param changePasswordDTO
     * @return Returns an "ok" response if the address update is successful
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @PutMapping("/change-password-by-token")
    public ResponseEntity<?> changePasswordByToken(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        this.userService.changePasswordByToken(changePasswordDTO);
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_OK,
                messageSource.getMessage("success.passwordChange", null, null), null));
    }

    /**
     * Method show all user
     *
     * @param no
     * @param limit
     * @return show list UserDTO
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @GetMapping("")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = PageDefault.NO) int no,
                                         @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return new ResponseEntity<>(this.userService.findAllPagination(no, limit), HttpStatus.ACCEPTED);
    }

    /**
     * Method search by name user
     *
     * @param keyword
     * @param no
     * @param limit
     * @return show list UserDTO
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @GetMapping("/search")
    public ResponseEntity<?> getUsersByKeyword(@RequestParam(required = false, defaultValue = "") String keyword,
                                               @RequestParam(defaultValue = PageDefault.NO) int no,
                                               @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return new ResponseEntity<>(this.userService.findAllByKeywordsPagination(keyword, no, limit),
                HttpStatus.ACCEPTED);
    }

    /**
     * Method lock user account
     *
     * @param id
     * @return
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PutMapping("/lock-user/{id}")
    public ResponseEntity<?> disableUser(@PathVariable long id) {

        this.userService.disable(id);
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_OK,
                messageSource.getMessage("success.lockUser", null, null), null));
    }

}
