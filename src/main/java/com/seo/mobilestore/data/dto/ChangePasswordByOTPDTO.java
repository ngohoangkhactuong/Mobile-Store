package com.seo.mobilestore.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordByOTPDTO implements Serializable {
    @NotNull(message = "{error.token}")
    private String otp;
    @NotNull(message = "{error.userNewPassword}")
    private String newPassword;
}
