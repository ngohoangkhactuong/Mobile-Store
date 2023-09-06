package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.common.MailResponse;
import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.common.enumeration.EMailType;
import com.seo.mobilestore.data.entity.User;
import com.seo.mobilestore.data.repository.UserRepository;
import com.seo.mobilestore.exception.InternalServerErrorException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.MailService;
import com.seo.mobilestore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private ServletContext context;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;


    private List<MimeMessage> queue = new ArrayList<MimeMessage>();
    @Value("${url.redirect.path}")
    private String urlRedirect;

    /**
     * Method send mail (option)
     *
     * @param response
     */
    @Override
    public void send(MailResponse response) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd%HH:mm:ss"));

        UUID uuid = UUID.randomUUID();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(response.getTo());

            // random otp
            Random random = new Random();
            int randomNumber = random.nextInt(9000) + 1000;

            //case send
            switch (response.getTypeMail()) {
                case ConfirmMailAndroid:
                    response.setSubject(randomNumber + " Xác thực email cho tài khoản App Mobile Store App");

                    userService.updateTokenActive(response.getTo(), String.valueOf(randomNumber));
                    response.createMailActiveOTP(urlRedirect, randomNumber);

                    break;

                case ForgotPasswordOTP:
                    response.setSubject(randomNumber + " Yêu cầu đổi mật khẩu tài khoản trên Mobile Store App");

                    userService.updateTokenForgetPassword(response.getTo(), String.valueOf(randomNumber));
                    response.createMailForgotPasswordOTP(urlRedirect, randomNumber);

                    break;
            }
            helper.setSubject(response.getSubject());
            helper.setText(response.getMailTemplate(), true);

            queue.add(message);

        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

    }

    /**
     * Method send mail type ConfirmMailAndroid: active user
     *
     * @param email
     * @return Returns an "ok" response if the address update is successful
     */
    @Override
    public MessageResponse sendMailActive(String email) {
        User user = userService.findByEmail(email);
        if (user.isStatus() == true)
            throw new InternalServerErrorException(messageSource.getMessage("error.alreadyActive", null, null));
        MailResponse mail = new MailResponse();
        mail.setTo(email);// Set email to reset password! Get User by Email => Change Password
        mail.setTypeMail(EMailType.ConfirmMailAndroid);
        this.send(mail);
        return new MessageResponse(HttpServletResponse.SC_OK, "SEND MAIL", null);
    }

    /**
     * Method send mail type ForgotPasswordOTP: change password user
     *
     * @param email
     * @return Returns an "ok" response if the address update is successful
     */
    @Override
    public MessageResponse sendMailForgotPassword(String email) {
        if (!userRepository.existsByEmail(email))
            throw new ResourceNotFoundException(Collections.singletonMap("User email", email));
        MailResponse mail = new MailResponse();
        mail.setTo(email);// Set email to reset password! Get User by Email => Change Password

        mail.setTypeMail(EMailType.ForgotPasswordOTP);
        this.send(mail);
        return new MessageResponse(HttpServletResponse.SC_OK, "SEND MAIL", null);
    }

    /**
     * Method delay time to send mail
     */
    @Scheduled(fixedDelay = 10000) // DELAY 10s
    public void run() {
        boolean flag = false;

        while (!queue.isEmpty()) {
            MimeMessage message = queue.remove(0);

            try {
                javaMailSender.send(message);
                flag = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (flag)
            System.out.println("Send mail successfully");
    }

}

