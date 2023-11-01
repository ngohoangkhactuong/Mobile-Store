package com.seo.mobilestore.data.mapper.product;

import com.seo.mobilestore.data.dto.product.ProductCreationDTO;
import com.seo.mobilestore.data.dto.product.ProductDTO;
import com.seo.mobilestore.data.dto.product.ShowProductDTO;
import com.seo.mobilestore.data.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring", uses = {CategoriesMapper.class, ManufacturerMapper.class})
@Component
public interface ProductMapper {

    @Mapping(ignore = true, target = "category")
    @Mapping(ignore = true, target = "manufacturer")
    Product toEntity(ProductDTO productDTO);

    @Mapping(ignore = true, target = "category")
    @Mapping(ignore = true, target = "manufacturer")
    Product toProductEntity(ProductCreationDTO productCreationDTO);

    @Mapping(source = "category", target = "categoriesDTO")
    @Mapping(source = "manufacturer", target = "manufacturerDTO")
//    @Mapping(source = "productTechs", target = "productTechDTOs")
    // @Mapping(source = "series", target = "seriDTOs")
//    @Mapping(source = "colors", target = "colorDTOs")
    @Mapping(source = "memories", target = "memoryDTOs")
    @Mapping(source = "reviews", target = "reviewDTOs")
    @Mapping(source = "images", target = "imageDTOs")
    ProductDTO toDTO(Product product);

    @Mapping(source = "category", target = "categoriesDTO")
    @Mapping(source = "manufacturer", target = "manufacturerDTO")
//    @Mapping(source = "series.color.name", target = "seriDTOs.color")
    @Mapping(source = "memories", target = "memoryDTOs")
    @Mapping(source = "images", target = "imageDTOs")
    ProductCreationDTO toProductDTO(Product product);

    @Mapping(source = "category", target = "categoriesDTO")
    @Mapping(source = "manufacturer", target = "manufacturerDTO")
    @Mapping(source = "images", target = "imageDTOs")
    ShowProductDTO toShowProductDTO(Product product);
}

