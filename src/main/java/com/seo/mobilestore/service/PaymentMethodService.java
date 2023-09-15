package com.seo.mobilestore.service;

import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.paymentMethod.PaymentMethodDTO;

import java.util.List;

public interface PaymentMethodService {
    MessageResponse create(PaymentMethodDTO paymentMethodDTO);

    PaymentMethodDTO update(long id,PaymentMethodDTO paymentMethodDTO);

    boolean deleteById(long id);
    
    PaymentMethodDTO getById(long id);
    
    PaginationDTO getAllPagination(int no, int limit);
}
