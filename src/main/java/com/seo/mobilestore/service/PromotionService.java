package com.seo.mobilestore.service;


import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.promotion.PromotionCreationDTO;
import com.seo.mobilestore.data.dto.promotion.PromotionDTO;

public interface PromotionService {
    PromotionDTO create(PromotionCreationDTO promotionCreationDTO);

    PromotionDTO update(long id, PromotionCreationDTO promotionCreationDTO);

    PromotionDTO getById(long id);

    PaginationDTO getAll(int no , int limit);

    PaginationDTO findAllByDiscountCode(String discountCode,int no,int limit);

    boolean deleteById(long id);
}
