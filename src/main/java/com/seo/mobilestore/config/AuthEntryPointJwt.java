package com.seo.mobilestore.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    private static final String MESSAGE_UNAUTHORIZED = "Unauthorized";
    private static final String MESSAGE_FORBIDDEN = "Forbidden";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        LOGGER.error("Unauthorized error: {}", authException.getMessage());

        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        String errorMessage = MESSAGE_UNAUTHORIZED;

        if (authException instanceof InsufficientAuthenticationException) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            errorMessage = MESSAGE_FORBIDDEN;
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(statusCode);

        final Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", statusCode);
        responseBody.put("error", errorMessage);
        responseBody.put("message", authException.getMessage());
        responseBody.put("path", request.getServletPath());

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), responseBody);
    }
}
