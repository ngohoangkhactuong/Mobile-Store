package com.seo.mobilestore.common.util.oauth;


import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;
    protected  String clientName;
    public OAuth2UserInfo(Map<String, Object> attributes,String clientName) {
        this.attributes = attributes;
        this.clientName=clientName;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getAvatar();
    public abstract String getFistName();
    public abstract String getLastName();

    public abstract String getClientName();
    public abstract String getClientId();

    public abstract String getLocale();

}

