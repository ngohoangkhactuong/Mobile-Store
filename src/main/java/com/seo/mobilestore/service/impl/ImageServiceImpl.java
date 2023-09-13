package com.seo.mobilestore.service.impl;


import com.seo.mobilestore.data.dto.ImageDTO;
import com.seo.mobilestore.data.entity.Image;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.mapper.product.ImageMapper;
import com.seo.mobilestore.data.repository.ImageRepository;
import com.seo.mobilestore.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public List<ImageDTO> createProductImage(Product product, List<ImageDTO> imageDTOs){

        List<ImageDTO> imageDTOsResult = new ArrayList<>();

        imageDTOs.forEach(imageDTO -> {
            Image image = imageMapper.toEntity(imageDTO);
            image.setProduct(product);

            imageDTOsResult.add(imageMapper.toDTO(imageRepository.save(image)));
        });

        return imageDTOsResult;
    }

}
