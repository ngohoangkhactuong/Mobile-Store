package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.data.dto.product.seri.SeriDTO;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.entity.Seri;
import com.seo.mobilestore.data.mapper.product.SeriMapper;
import com.seo.mobilestore.data.repository.SeriRepository;
import com.seo.mobilestore.service.SeriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeriServiceImpl implements SeriService {

    @Autowired
    private SeriMapper seriMapper;
    @Autowired
    private SeriRepository seriRepository;

    @Override
    public List<SeriDTO> createProductSeri(Product product, List<SeriDTO> seriDTOs) {

        List<SeriDTO> seriDTOsResult = new ArrayList<>();
        seriDTOs.forEach(seriDTO -> {

            Seri seri = seriMapper.toEntity(seriDTO);
            seri.setProduct(product);
            seriDTOsResult.add(seriMapper.toDTO(seriRepository.save(seri)));
        });
        return seriDTOsResult;
    }
}

