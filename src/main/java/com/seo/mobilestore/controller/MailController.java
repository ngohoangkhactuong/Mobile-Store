package com.seo.mobilestore.controller;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.service.MailService;
import com.seo.mobilestore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiURL.MAIL)
public class MailController {
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;

    /**
     * Method send mail active to user account
     * @param email
     * @return Returns an "ok" response if the address update is successful
     */
    @GetMapping("/active-user")
    public ResponseEntity<?> sendMailActive(@RequestParam String email) {
        return ResponseEntity.ok(mailService.sendMailActive(email));
    }

    /**
     * Method send mail otp for change password to user account
     * @return Returns an "ok" response if the address update is successful
     */
    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        return ResponseEntity.ok(mailService.sendMailForgotPassword(email));
    }
}
