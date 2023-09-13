package com.seo.mobilestore.service.impl;


import com.seo.mobilestore.data.dto.product.color.ColorDTO;
import com.seo.mobilestore.data.entity.Color;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.mapper.product.ColorMapper;
import com.seo.mobilestore.data.repository.ColorRepository;
import com.seo.mobilestore.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private ColorMapper colorMapper;
    @Override
    public List<ColorDTO> createProductColor(Product product, List<ColorDTO> colorDTOs) {

        List<ColorDTO> colorDTOsResult = new ArrayList<>();

        colorDTOs.forEach(colorDTO -> {
            Color color = colorMapper.toEntity(colorDTO);
            color.setProduct(product);
            color.setStatus(true);

            colorDTOsResult.add(colorMapper.toDTO(colorRepository.save(color)));
        });

        return colorDTOsResult;
    }

}
