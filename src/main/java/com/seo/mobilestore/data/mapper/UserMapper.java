package com.seo.mobilestore.data.mapper;

import com.seo.mobilestore.data.dto.user.UserCreationDTO;
import com.seo.mobilestore.data.dto.user.UserDTO;
import com.seo.mobilestore.data.dto.user.UserProfileDTO;
import com.seo.mobilestore.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = { RoleMapper.class, StatusMapper.class })
@Component
public interface UserMapper {

	@Mapping(ignore = true, target = "password")
    User toEntity(UserCreationDTO creationDTO);

	User toEntity(UserProfileDTO userProfileDTO);

	@Mapping(source = "role", target = "roleDTO")
	@Mapping(source = "status", target = "statusDTO")
	@Mapping(source = "lock_status", target = "lockStatusDTO")
	UserDTO toDTO(User user);

}
