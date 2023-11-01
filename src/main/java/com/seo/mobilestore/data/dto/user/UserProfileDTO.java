package com.seo.mobilestore.data.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seo.mobilestore.constant.Constant;
import com.seo.mobilestore.data.dto.address.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserProfileDTO implements Serializable {
    private String email;
    private String fullName;
    private int gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_FORMAT)
    private Date birthDay;
}
