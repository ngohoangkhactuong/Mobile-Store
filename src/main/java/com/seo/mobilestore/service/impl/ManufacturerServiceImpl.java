package com.seo.mobilestore.service.impl;


import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.data.dto.ImageDTO;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.ProductDTO;
//import com.seo.mobilestore.data.dto.product.ProductTechDTO;
import com.seo.mobilestore.data.dto.product.categories.CategoriesDTO;
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
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


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

    @Override
    public ManufacturerDTO update(long id , ManufacturerDTO manufacturerDTO){
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("Not found" , id)));

        Map<String, Object> errors = new HashMap<>();

        if (manufacturerRepository.existsByName(manufacturerDTO.getName())) {
            errors.put("name", manufacturerDTO.getName());
        }

        if (!errors.isEmpty()) {
            throw new ConflictException(Collections.singletonMap("manufacturerDTO", errors));
        }

        manufacturer.setName(manufacturerDTO.getName());

        this.manufacturerRepository.save(manufacturer);

        return manufacturerMapper.toDTO(manufacturer);
    }


    @Override
    public Boolean delete(long id){
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("Not found" , id)));

        boolean deleteStatus = false;
        manufacturer.setStatus(deleteStatus);
        this.manufacturerRepository.save(manufacturer);

        return true;
    }

    @Override
    public ManufacturerDTO getById(long id){
        Manufacturer manufacturer = manufacturerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        return manufacturerMapper.toDTO(manufacturer);
    }

    @Override
    public PaginationDTO getAllPagination(int no, int limit) {
        Page<ManufacturerDTO> page = this.manufacturerRepository.findAll(
                PageRequest.of(no, limit)).map(item -> manufacturerMapper.toDTO(item));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(), page.getSize(),
                page.getNumber());
    }
}


