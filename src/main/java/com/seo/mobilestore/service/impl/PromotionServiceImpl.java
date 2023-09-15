package com.seo.mobilestore.service.impl;


import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.promotion.PromotionCreationDTO;
import com.seo.mobilestore.data.dto.promotion.PromotionDTO;
import com.seo.mobilestore.data.entity.Promotion;
import com.seo.mobilestore.data.mapper.PromotionMapper;
import com.seo.mobilestore.data.repository.OrdersRepository;
import com.seo.mobilestore.data.repository.PromotionRepository;
import com.seo.mobilestore.exception.CannotDeleteException;
import com.seo.mobilestore.exception.ConflictException;
import com.seo.mobilestore.exception.ResourceNotFoundException;
import com.seo.mobilestore.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private PromotionMapper promotionMapper;
    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public PromotionDTO create(PromotionCreationDTO promotionCreationDTO) {

        Map<String, Object> errors = new HashMap<>();

        if (promotionCreationDTO == null) {
            throw new NullPointerException();
        }

        if (this.promotionRepository.existsByDiscountCode(promotionCreationDTO.getDiscountCodeDTO())) {
            errors.put("Discount Code", promotionCreationDTO.getDiscountCodeDTO());
            throw new ConflictException(Collections.singletonMap("promotionCreationDTO", errors));
        }

        Promotion newPromotion = promotionMapper.toEntity(promotionCreationDTO);
        newPromotion.setStatus(true);

        return promotionMapper.toDTO(this.promotionRepository.save(newPromotion));
    }


    @Override
    public PromotionDTO update(long id, PromotionCreationDTO promotionCreationDTO) {

        if (ObjectUtils.isEmpty(promotionCreationDTO.getDiscountDTO())) {
            throw new NullPointerException();
        }

        Map<String, Object> errors = new HashMap<>();

        Promotion oldPromotion = this.promotionRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(Collections.singletonMap("Promotion ID", id)));

        if (this.promotionRepository.existsByDiscountCodeAndIdNot(promotionCreationDTO.getDiscountCodeDTO(), id)) {
            errors.put("Discount Code", promotionCreationDTO.getDiscountDTO());
            throw new ConflictException(Collections.singletonMap("promotionCreationDTO", errors));
        }

        Promotion updatePromotion = promotionMapper.toEntity(promotionCreationDTO);
        updatePromotion.setId(oldPromotion.getId());
        updatePromotion.setStatus(true);

        return this.promotionMapper.toDTO(this.promotionRepository.save(updatePromotion));
    }


    @Override
    public PromotionDTO getById(long id) {

        if (ObjectUtils.isEmpty(id)) {
            throw new NullPointerException();
        }

        Promotion promotion = this.promotionRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(Collections.singletonMap("Promotion Id", id)));

        //Check if the product has been deleted (hide by status)
        if (!promotion.isStatus()) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        return this.promotionMapper.toDTO(promotion);
    }


    public PaginationDTO getAll(int no, int limit) {

        if (ObjectUtils.isEmpty(no) || ObjectUtils.isEmpty(limit)) {
            throw new NullPointerException();
        }

        Page<PromotionDTO> page = this.promotionRepository.findByStatusIsTrue(PageRequest.of(no, limit))
                .map(p -> this.promotionMapper.toDTO(p));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages()
                , page.getTotalElements(), page.getSize(), page.getNumber());
    }

    @Override
    public PaginationDTO findAllByDiscountCode(String discountCode, int no, int limit) {

        if (ObjectUtils.isEmpty(discountCode) || ObjectUtils.isEmpty(no) || ObjectUtils.isEmpty(limit)) {
            throw new NullPointerException();
        }

        Page<PromotionDTO> page = this.promotionRepository.findAllByDiscountCodeContainingAndStatusIsTrue(
                discountCode, PageRequest.of(no, limit)).map(p -> this.promotionMapper.toDTO(p));

        return new PaginationDTO(page.getContent(), page.isFirst(), page.isLast(), page.getTotalPages(),
                page.getTotalElements(), page.getSize(), page.getNumber());
    }


    @Override
    public boolean deleteById(long id) {

        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        if (!promotion.isStatus()) {
            throw new ResourceNotFoundException(Collections.singletonMap("id", id));
        }

        //Check if the promotion exists in any orders
        if (ordersRepository.existsByPromotionId(id)) {
            throw new CannotDeleteException(id);
        }

        promotion.setStatus(false);
        promotionRepository.save(promotion);

        return true;
    }
}
