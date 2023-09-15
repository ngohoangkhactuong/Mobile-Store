package com.seo.mobilestore.controller.mail;

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

    @GetMapping("/active-user")
    public ResponseEntity<?> sendMailActive(@RequestParam String email) {
        return ResponseEntity.ok(mailService.sendMailActive(email));
    }

    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        return ResponseEntity.ok(mailService.sendMailForgotPassword(email));
    }
}
