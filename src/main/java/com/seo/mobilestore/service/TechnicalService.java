package com.seo.mobilestore.service;


import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.TechnicalDTO;

public interface TechnicalService {

    TechnicalDTO createTechnical(TechnicalDTO technicalDTO);

    TechnicalDTO create(TechnicalDTO technicalDTO);

    TechnicalDTO create(String name);

}
