package com.seo.mobilestore.common.util.oauth;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes,String clientName) {
        super(attributes,clientName);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getAvatar() {
        return (String) attributes.get("picture");
    }
    @Override
    public String getFistName() {
        return (String) attributes.get("family_name");
    }


@Override
    public String getLastName() {
        return (String) attributes.get("given_name");
    }

    @Override
    public String getClientName() {
        return this.clientName;
    }

    @Override
    public String getClientId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getLocale() {
        return (String) attributes.get("locale");
    }
}