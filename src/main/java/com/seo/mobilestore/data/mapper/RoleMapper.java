package com.seo.mobilestore.data.mapper;

import com.seo.mobilestore.data.dto.RoleDTO;
import com.seo.mobilestore.data.entity.Role;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface RoleMapper {
    Role toEntity(RoleDTO roleDTO);

    RoleDTO toDTO(Role role);

}
