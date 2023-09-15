package com.seo.mobilestore.service.impl.product;


import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.TechnicalDTO;
import com.seo.mobilestore.data.dto.product.ProductTechDTO;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.entity.ProductTech;
import com.seo.mobilestore.data.entity.Technical;
import com.seo.mobilestore.data.mapper.product.ProductTechMapper;
import com.seo.mobilestore.data.mapper.product.TechnicalMapper;
import com.seo.mobilestore.data.repository.ProductTechRepository;
import com.seo.mobilestore.exception.InternalServerErrorException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
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
    @Autowired
    private MessageSource  messageSource;

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

    @Override
    public ProductTechDTO create(ProductTechDTO productTechDTO) {

        return productTechMapper.toDTO(productTechRepository.save(productTechMapper.toEntity(productTechDTO)));
    }
    @Override
    @Transactional
    public List<ProductTechDTO> updateProductTech(Product product, List<ProductTechDTO> productTechDTOs){


        List<ProductTechDTO> productTechDTOsResult = new ArrayList<>();
        List<ProductTech> productTechs = productTechRepository.findByProductId(product.getId());
        List<ProductTechDTO> productTechDTOsPrepare = new ArrayList<>();

        productTechs.forEach(productTech -> {
            productTechDTOsPrepare.add(productTechMapper.toDTO(productTech));
            //If product tech existed in database and not exist in input new Product Tech DTO then update status = 0
            if (!productTechDTOs.contains(productTechMapper.toDTO(productTech))){
                // Update status = false (0) for productTech when consumer delete this productTech
                productTech.setStatus(false);
                productTechRepository.save(productTech);
            } else {
                //if product tech existed in database and existed in input then update the information of product tech
                productTechDTOs.forEach(productTechDTO -> {
                    // if productTech existed in database, it will be updated then save it
                    if (productTechDTO.getId() == productTech.getId()) {

                        ProductTech productTechMapped=  productTechMapper.toEntity(productTechDTO);
                        productTechMapped.setProduct(product);

                        Technical technical = technicalMapper.toEntity(productTechDTO.getTechnicalDTO());
                        productTechMapped.setTechnical(technical);

                        productTechDTOsResult.add(productTechMapper.toDTO(productTechRepository.save(productTechMapped)));
                    }
                });
            }
        });
        // a foreach to check a productTechDTO in list productTechDTO have the new productTech, yes or no?
        // if existing new productTech then save this productTech into database
        productTechDTOs.forEach(productTechDTO -> {
            if (!productTechDTOsPrepare.contains(productTechDTO)) {
                ProductTech productTechMapped=  productTechMapper.toEntity(productTechDTO);
                productTechMapped.setProduct(product);

                Technical technical = technicalMapper.toEntity(productTechDTO.getTechnicalDTO());
                productTechMapped.setTechnical(technical);

                ProductTech productTechSaved = productTechRepository.save(productTechMapped);
                productTechDTOsResult.add(productTechMapper.toDTO(productTechSaved));
            }
        });

        return productTechDTOsResult;
    }
    @Override
    public ProductTechDTO update(int id, String info) {

        ProductTech oldProductTech = this.productTechRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        oldProductTech.setInfo(info);

        return this.productTechMapper.toDTO(productTechRepository.save(oldProductTech));
    }
    @Override
    public boolean deleteById(int id) {

        ProductTech oldProductTech = this.productTechRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        //Check Product Tech deleted
        if (!oldProductTech.isStatus()) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        oldProductTech.setStatus(false);

        productTechRepository.save(oldProductTech);

        return true;
    }

    @Override
    public ProductTechDTO getById(int id){
        ProductTech productTech = productTechRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        if(!productTech.isStatus())
            throw new InternalServerErrorException(messageSource.getMessage("info.notActive",
                    null, null));

        return productTechMapper.toDTO(productTech);
    }

    @Override
    public PaginationDTO getAllPagination(int no, int limit) {
        Page<ProductTechDTO> page = this.productTechRepository.findAll(
                PageRequest.of(no, limit)).map(item -> productTechMapper.toDTO(item));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(), page.getSize(),
                page.getNumber());
    }

}
