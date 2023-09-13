package com.seo.mobilestore.data.mapper.product;

import com.seo.mobilestore.data.dto.product.categories.CategoriesDTO;
import com.seo.mobilestore.data.entity.Categories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CategoriesMapper {

    @Mapping(target = "status",constant = "true")
    Categories toEntity(CategoriesDTO categoriesDTO);

    CategoriesDTO toDTO(Categories categories);
}

