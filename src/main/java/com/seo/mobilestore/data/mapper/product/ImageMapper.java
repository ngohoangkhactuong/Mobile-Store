package com.seo.mobilestore.data.mapper.product;

import com.seo.mobilestore.data.dto.ImageDTO;
import com.seo.mobilestore.data.entity.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    Image toEntity(ImageDTO imageDTO);

    ImageDTO toDTO(Image image);
}
