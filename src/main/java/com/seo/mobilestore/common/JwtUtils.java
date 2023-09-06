package com.seo.mobilestore.common;

import java.util.*;
import java.util.stream.Collectors;

import com.seo.mobilestore.config.AppProperties;

import com.seo.mobilestore.common.util.oauth.UserPrincipal;
import com.seo.mobilestore.data.entity.Role;
import com.seo.mobilestore.data.entity.User;
import com.seo.mobilestore.data.repository.UserRepository;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.RoleService;
import com.seo.mobilestore.service.impl.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
    @Value("${r2s.jwtSecret}")
    private String jwtSecret;
    @Value("${r2s.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;
    private AppProperties appProperties;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(userPrincipal.getUsername());
        if (authentication.getAuthorities().size() == 0) {
            List<Role> roles = new ArrayList<>();
            User user = userRepository.findByEmail(userPrincipal.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("email", userPrincipal.getEmail())));
            roles.add(user.getRole());
            claims.put("roles", roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                    .collect(Collectors.toList()));

        } else {
            claims.put("roles", authentication.getAuthorities().stream()
                    .map(item -> new SimpleGrantedAuthority(item.getAuthority())).collect(Collectors.toList()));
        }
        return Jwts.builder()
                .setClaims(claims).setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();

    }


    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(userPrincipal.getUsername());
        claims.put("roles", userPrincipal.getAuthorities().stream().map(item -> new SimpleGrantedAuthority(item.getAuthority())).collect(Collectors.toList()));
        return Jwts.builder()
                .setClaims(claims).setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    // lấy role từ token
    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getRolesFromToken(String token) {
        // ví dụ:
        // "roles": [
        // {
        // "authority" : "Role_Admin"
        // }
        // ]
        return (List<Map<String, String>>) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody()
                .get("roles");
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {

        }
        return false;

    }

}
