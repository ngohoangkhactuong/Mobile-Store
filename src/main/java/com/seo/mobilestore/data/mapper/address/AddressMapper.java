package com.seo.mobilestore.data.mapper.address;

import com.seo.mobilestore.data.dto.address.AddressDTO;
import com.seo.mobilestore.data.dto.address.AddressShowDTO;
import com.seo.mobilestore.data.entity.Address;
import com.seo.mobilestore.data.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring", uses = { UserMapper.class})
@Component
public interface AddressMapper {
    @Mapping(source = "location", target = "location")
    @Mapping(source = "phoneReceiver", target = "phoneReceiver")
    @Mapping(source = "nameReceiver", target = "nameReceiver")
    @Mapping(source = "defaults", target = "defaults")
    @Mapping(source = "status", target = "status")
    AddressDTO toDTO(Address address);
    AddressShowDTO toShowDTO(Address address);

    @Mapping(source = "location", target = "location")
    @Mapping(source = "phoneReceiver", target = "phoneReceiver")
    @Mapping(source = "nameReceiver", target = "nameReceiver")
    @Mapping(source = "defaults", target = "defaults")
    @Mapping(target = "status",constant = "true")
    Address toEnity(AddressDTO addressDTO);
}
