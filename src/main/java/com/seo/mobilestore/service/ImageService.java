package com.seo.mobilestore.service;

import com.seo.mobilestore.data.dto.ImageDTO;
import com.seo.mobilestore.data.entity.Product;

import java.util.List;

public interface ImageService {
    List<ImageDTO> createProductImage(Product product, List<ImageDTO> imageDTOs);

    ImageDTO create(ImageDTO imageDTO);

    List<ImageDTO> updateProductImage(Product product, List<ImageDTO> imageDTOs);
}
