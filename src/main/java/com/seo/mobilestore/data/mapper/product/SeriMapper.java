package com.seo.mobilestore.data.mapper.product;

import com.seo.mobilestore.data.dto.product.seri.SeriDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriProductDTO;
import com.seo.mobilestore.data.entity.Seri;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ProductMapper.class})
public interface SeriMapper {

    Seri toEntity(SeriDTO seriDTO);

    SeriDTO toDTO(Seri seri);

    @Mapping(source = "name" , target = "name")
    @Mapping(source = "status" , target = "status")
    @Mapping(source = "id" , target = "id")
    @Mapping(source = "product_id" , target = "product.id")
    Seri toSeriEntity(SeriProductDTO seriProductDTO);

    @Mapping(source = "name" , target = "name")
    @Mapping(source = "status" , target = "status")
    @Mapping(source = "id" , target = "id")
    @Mapping(source = "product.id" , target = "product_id")
    SeriProductDTO toSeriProductDTO(Seri seri);

}

