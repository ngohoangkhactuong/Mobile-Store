package com.seo.mobilestore.common.util.oauth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final Object credentials;
    private final String email;
    private final String authProvider;

    public CustomAuthenticationToken(String email, String authProvider) {
        super(null);
        this.principal = email;
        this.credentials = null;
        this.email = email;
        this.authProvider = authProvider;
        setAuthenticated(false);
    }

    public CustomAuthenticationToken(String email, String authProvider, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = email;
        this.credentials = null;
        this.email = email;
        this.authProvider = authProvider;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getEmail() {
        return email;
    }

    public String getAuthProvider() {
        return authProvider;
    }
}