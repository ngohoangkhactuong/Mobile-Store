package com.seo.mobilestore.common.util.oauth;
import com.seo.mobilestore.service.impl.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof CustomAuthenticationToken) {
            // Xử lý xác thực cho CustomAuthenticationToken
            CustomAuthenticationToken customToken = (CustomAuthenticationToken) authentication;
            // Thực hiện xác thực tùy chỉnh dựa trên email và authProvider
            // ...
            // Tạo danh sách các quyền
            List<GrantedAuthority> authorities = new ArrayList<>();
            // Thêm các quyền cần thiết vào danh sách authorities
            // ...
            // Tạo instance của UserDetailsImpl để trả về
            UserDetailsImpl userDetails = new UserDetailsImpl(customToken.getEmail(), customToken.getAuthProvider(), authorities);
            // Tạo instance của UsernamePasswordAuthenticationToken để trả về
            return new UsernamePasswordAuthenticationToken(userDetails, customToken.getCredentials(), authorities);
        }

        throw new UnsupportedOperationException("Unsupported authentication token: " + authentication.getClass());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
