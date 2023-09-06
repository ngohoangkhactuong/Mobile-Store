package com.seo.mobilestore.data.dto.user;

import com.seo.mobilestore.data.dto.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDTO extends UserProfileDTO{

    private Long id;
    private String authProvider;
    private RoleDTO roleDTO;
    private boolean statusDTO;
    private boolean lockStatusDTO;
}
