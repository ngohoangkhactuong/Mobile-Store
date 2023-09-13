package com.seo.mobilestore.service.impl;


import com.seo.mobilestore.data.dto.TechnicalDTO;
import com.seo.mobilestore.data.entity.Technical;
import com.seo.mobilestore.data.mapper.product.TechnicalMapper;
import com.seo.mobilestore.data.repository.TechnicalRepository;
import com.seo.mobilestore.exception.ValidationException;
import com.seo.mobilestore.service.TechnicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
}
