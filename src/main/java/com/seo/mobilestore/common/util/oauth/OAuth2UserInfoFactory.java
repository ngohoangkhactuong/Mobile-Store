package com.seo.mobilestore.common.util.oauth;

import com.seo.mobilestore.common.enumeration.EAuthenticationProvider;
import com.seo.mobilestore.exception.ExceptionCustom;

import java.util.Map;
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes,String clientName) {
        if(registrationId.equalsIgnoreCase(EAuthenticationProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes,clientName);
        }
        if(registrationId.equalsIgnoreCase(EAuthenticationProvider.FACEBOOK.toString())) {
            return new GoogleOAuth2UserInfo(attributes,clientName);
        }else {
            throw new ExceptionCustom("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
