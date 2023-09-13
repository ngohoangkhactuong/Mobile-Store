package com.seo.mobilestore.service.impl.product;


import com.seo.mobilestore.data.dto.TechnicalDTO;
import com.seo.mobilestore.data.dto.product.ProductTechDTO;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.entity.ProductTech;
import com.seo.mobilestore.data.mapper.product.ProductTechMapper;
import com.seo.mobilestore.data.mapper.product.TechnicalMapper;
import com.seo.mobilestore.data.repository.ProductTechRepository;
import com.seo.mobilestore.service.ProductTechService;
import com.seo.mobilestore.service.TechnicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductTechServiceImpl implements ProductTechService {
    @Autowired
    private ProductTechRepository productTechRepository;
    @Autowired
    private TechnicalService technicalService;
    @Autowired
    private ProductTechMapper productTechMapper;
    @Autowired
    private TechnicalMapper technicalMapper;

    @Override
    public List<ProductTechDTO> createProductTech(Product product, List<ProductTechDTO> productTechDTOs){

        List<ProductTechDTO> productTechDTOsResult = new ArrayList<>();

        productTechDTOs.forEach(productTechDTO -> {
            TechnicalDTO newTechnicalDTO = technicalService.createTechnical(productTechDTO.getTechnicalDTO());

            ProductTech productTech = productTechMapper.toEntity(productTechDTO);
            productTech.setTechnical(technicalMapper.toEntity(newTechnicalDTO));
            productTech.setProduct(product);

            productTechDTOsResult.add(productTechMapper.toDTO(productTechRepository.save(productTech)));
        });

        return productTechDTOsResult;
    }

}
