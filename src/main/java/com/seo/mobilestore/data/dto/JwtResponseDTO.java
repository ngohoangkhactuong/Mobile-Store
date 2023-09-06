package com.seo.mobilestore.data.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtResponseDTO implements Serializable {
	private String token;
	private String type = "Bearer";
	private String email;
	private String role;
	private long idUser;
	private String message;

	public JwtResponseDTO(String accessToken, String email, String roles, long id,
			String message) {
		this.idUser = id;
		this.token = accessToken;
		this.email = email;
		this.role = roles;
		this.message = message;
	}
}
