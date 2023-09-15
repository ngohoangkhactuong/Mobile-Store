package com.seo.mobilestore.service.impl;


import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.TechnicalDTO;
import com.seo.mobilestore.data.entity.Technical;
import com.seo.mobilestore.data.mapper.product.TechnicalMapper;
import com.seo.mobilestore.data.repository.TechnicalRepository;
import com.seo.mobilestore.exception.ConflictException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.exception.ValidationException;
import com.seo.mobilestore.service.TechnicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class TechnicalServiceImpl implements TechnicalService {

    @Autowired
    private TechnicalRepository technicalRepository;
    @Autowired
    private TechnicalMapper technicalMapper;


    @Override
    public TechnicalDTO create(TechnicalDTO technicalDTO) {
        return technicalMapper.toDTO(technicalRepository.save(technicalMapper.toEntity(technicalDTO)));
    }


    @Override
    public TechnicalDTO createTechnical(TechnicalDTO technicalDTO) {

        Technical technical = technicalMapper.toEntity(technicalDTO);

        return technicalMapper.toDTO(technicalRepository.save(technical));
    }

    @Override
    public TechnicalDTO create(String name) {

        if(name.isEmpty())
            throw new ValidationException(Collections.singletonMap("name technical", name));

        if(technicalRepository.existsByName(name))
            throw new IllegalArgumentException("error.dataExist");

        Technical technical = new Technical();
        technical.setName(name);

        return technicalMapper.toDTO(technicalRepository.save(technical));
    }

    @Override
    public TechnicalDTO update(int id, TechnicalDTO technicalDTO) {

        Map<String, Object> errors = new HashMap<>();

        Technical foundTechnical = this.technicalRepository.findById((long) id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        //Check if exits name
        if(technicalRepository.existsByName(technicalDTO.getName())) {
            errors.put("name", technicalDTO.getName());
        }

        //throw conflict exception
        if (errors.size() > 0) {
            throw new ConflictException(Collections.singletonMap("TechnicalDTO", errors));
        }

        Technical updateTechnical = technicalMapper.toEntity(technicalDTO);
        updateTechnical.setId(foundTechnical.getId());

        return this.technicalMapper.toDTO(this.technicalRepository.save(updateTechnical));
    }

    @Override
    public TechnicalDTO getById(int id){
        Technical technical = technicalRepository.findById((long) id).orElseThrow(
                () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        return technicalMapper.toDTO(technical);
    }

    @Override
    public PaginationDTO getAllPagination(int no, int limit) {
        Page<TechnicalDTO> page = this.technicalRepository.findAll(
                PageRequest.of(no, limit)).map(item -> technicalMapper.toDTO(item));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(), page.getSize(),
                page.getNumber());
    }

}
