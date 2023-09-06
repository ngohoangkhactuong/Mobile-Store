package com.seo.mobilestore.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDTO implements Serializable {
	private String email;
	private String password;
	private String phoneNumber;
	private String authProvider;
}
