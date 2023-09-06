package com.seo.mobilestore.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seo.mobilestore.data.entity.Role;
import com.seo.mobilestore.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UserDetailsImpl implements UserDetails {

	private String email;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(String email) {
		this.email = email;
		this.password = ""; // Set a default or placeholder password value
		// Initialize other properties as needed
	}

	public static UserDetailsImpl build(User user) {
		List<Role> roles = new ArrayList<>();
		roles.add(user.getRole());
		List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().toString()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(user.getEmail(), user.getPassword(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
