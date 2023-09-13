package com.seo.mobilestore.data.mapper.product;

import com.seo.mobilestore.data.dto.product.manufacturer.ManufacturerDTO;
import com.seo.mobilestore.data.entity.Manufacturer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {

    @Mapping(target = "status",constant = "true")
    Manufacturer toEntity(ManufacturerDTO manufacturerDTO);

    ManufacturerDTO toDTO(Manufacturer manufacturer);
}
