package com.seo.mobilestore.service.impl;


import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.data.dto.ImageDTO;
import com.seo.mobilestore.data.dto.product.ProductDTO;
import com.seo.mobilestore.data.dto.product.ProductTechDTO;
import com.seo.mobilestore.data.dto.product.color.ColorDTO;
import com.seo.mobilestore.data.dto.product.manufacturer.ManufacturerDTO;
import com.seo.mobilestore.data.dto.product.memory.MemoryDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriDTO;
import com.seo.mobilestore.data.entity.Categories;
import com.seo.mobilestore.data.entity.Manufacturer;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.mapper.product.CategoriesMapper;
import com.seo.mobilestore.data.mapper.product.ManufacturerMapper;
import com.seo.mobilestore.data.mapper.product.ProductMapper;
import com.seo.mobilestore.data.repository.ManufacturerRepository;
import com.seo.mobilestore.data.repository.ProductRepository;
import com.seo.mobilestore.exception.ConflictException;
import com.seo.mobilestore.exception.InternalServerErrorException;
import com.seo.mobilestore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;


@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Override
    @Transactional
    public MessageResponse create(ManufacturerDTO manufacturerDTO) {

        Manufacturer manufacturer = manufacturerMapper.toEntity(manufacturerDTO);

        if (manufacturerRepository.existsByName(manufacturerDTO.getName())){
            throw new InternalServerErrorException(String.format("Exists manufacturer named %s", manufacturer.getName()));
        }
        manufacturerRepository.save(manufacturer);
        return new MessageResponse(HttpServletResponse.SC_OK, "Created Manufacturer", null);
    }
}


