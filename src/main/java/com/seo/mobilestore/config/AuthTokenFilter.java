package com.seo.mobilestore.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

// import com.r2s.mobilestore.service.UserService;
import com.seo.mobilestore.common.JwtUtils;



public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;
	// @Autowired
	// private UserService userService;
	// @Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try
		{
			String jwt = parseJwt(request);
			if(jwt != null && jwtUtils.validateJwtToken(jwt))
			{
				String username = jwtUtils.getUsernameFromToken(jwt);
				// lấy roles từ token
				List<Map<String, String>> authorities = jwtUtils.getRolesFromToken(jwt);
				// đưa về dạng simplegrantedauthority
				Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
						.map(item -> new SimpleGrantedAuthority(item.get("authority"))).collect(Collectors.toSet());
				Authentication auth = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			filterChain.doFilter(request, response);
		}
		catch (JwtException e) {
			// nếu token không an toàn (đã qua edit) thì sẽ throw exception
			throw new IllegalStateException("Token cannot be trusted");
		}
	}
	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer "))
		{
			return headerAuth.substring(7);
		}
		return null;
	}
	
}
