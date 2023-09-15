package com.seo.mobilestore.service;


import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.TechnicalDTO;

public interface TechnicalService {

    TechnicalDTO createTechnical(TechnicalDTO technicalDTO);

    TechnicalDTO create(TechnicalDTO technicalDTO);

    TechnicalDTO create(String name);

    TechnicalDTO update(int id, TechnicalDTO technicalDTO);

    TechnicalDTO getById(int id);

    PaginationDTO getAllPagination(int no, int limit);


}
