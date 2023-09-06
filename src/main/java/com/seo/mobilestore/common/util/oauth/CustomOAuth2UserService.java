package com.seo.mobilestore.common.util.oauth;

import java.util.Optional;

import com.seo.mobilestore.exception.ExceptionCustom;
import com.seo.mobilestore.service.RoleService;
import com.seo.mobilestore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
//import com.r2s.mobilestore.data.entity.Candidate;
import com.seo.mobilestore.data.entity.User;
//import com.r2s.mobilestore.data.repository.CandidateRepository;
import com.seo.mobilestore.data.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private CandidateRepository candidateRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageSource messageSource;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String clientName=userRequest.getClientRegistration().getClientName();
        System.out.println("CustomOAuth2UserService invoked");
        try {
            return processOAuth2User(userRequest, oAuth2User,clientName);
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            System.out.println(ex.getMessage());
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User,String clientName) {


        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes(),clientName);
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new ExceptionCustom("Email not found from OAuth2 provider");
        }

//        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmailAndRole(oAuth2UserInfo.getEmail(), roleService.findById(3))
//                        . orElseThrow(
//                () -> new InternalServerErrorException(this.messageSource.getMessage("error.checkEmail", null, null)))
//        );
        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user = new User();
//        Candidate candidate = new Candidate();

        if (userOptional.isPresent()) {

            user = userOptional.get();
//            if(!user.getAuthProvider().equals(EAuthenticationProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()).toString().toUpperCase())) {
//                throw new CustomException("Looks like you're signed up with " +
//                        user.getAuthProvider() + " account. Please use your " + user.getAuthProvider() +
//                        " account to login.");
//            }
            String role = roleService.findByName("Role_Candidate").getName();
//            if (user.getRole().getName().equals(role)) {
//
//                userService.updateAfterLoginOAuth(user, oAuth2UserInfo, EAuthenticationProvider.GOOGLE);
//            } else {
//
//                throw new InternalServerErrorException(this.messageSource.getMessage("error.checkEmail", null, null));
//            }
        } else {

//            user = userService.createAfterLoginOAuth(oAuth2UserInfo);
            user = userRepository.save(user);
//            candidate.setUser(user);
//            candidateRepository.save(candidate);
        }
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }


}
