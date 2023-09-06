package com.seo.mobilestore.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDTO implements Serializable {
	@NotNull(message = "{error.userNewPassword}")
	private String newPassword;
	@NotNull(message = "{error.userOldPassword}")
	private String oldPassword;
}
