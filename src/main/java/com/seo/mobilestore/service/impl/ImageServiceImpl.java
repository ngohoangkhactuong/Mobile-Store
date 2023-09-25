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
    public ImageDTO create(ImageDTO imageDTO) {
        return imageMapper.toDTO(imageRepository.save(imageMapper.toEntity(imageDTO)));
    }

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

    @Override
    @Transactional
    public List<ImageDTO> updateProductImage(Product product, List<ImageDTO> imageDTOs){

        List<ImageDTO> imageDTOsResult = new ArrayList<>();
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDTO> imageDTOsPrepare = new ArrayList<>();

        images.forEach(image -> {
            // add image mapped to prepare existing it in database
            imageDTOsPrepare.add(imageMapper.toDTO(image));
            // Check if the image exists in the database
            if (!imageDTOs.contains(imageMapper.toDTO(image))){
                // Update status = false (0) for image when consumer delete this image
                image.setStatus(false);
                imageRepository.save(image);
            } else {
                imageDTOs.forEach(imageDTO -> {
                    // if image existed in database, it will be updated then save it
                    if (imageDTO.getId() == image.getId()) {
                        Image imageMapped=  imageMapper.toEntity(imageDTO);
                        imageMapped.setProduct(product);
                        imageDTOsResult.add(imageMapper.toDTO(imageRepository.save(imageMapped)));
                    }
                });
            }
        });
        // a foreach to check a imageDTO in list imageDTO have the new image, yes or no?
        // if existing new image then save this image into database
        imageDTOs.forEach(imageDTO -> {
            if (imageDTO.getName() != null) {
                if (!imageDTOsPrepare.contains(imageDTO)) {

                    Image imageMapped=  imageMapper.toEntity(imageDTO);
                    imageMapped.setProduct(product);
                    Image imageSaved = imageRepository.save(imageMapped);

                    imageDTOsResult.add(imageMapper.toDTO(imageSaved));
                }
            }
        });

        return imageDTOsResult;
    }

}
