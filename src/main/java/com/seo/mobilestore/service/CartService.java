package com.seo.mobilestore.service;

import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.cart.CartDTO;
import com.seo.mobilestore.data.dto.product.cart.ShowCartDTO;

public interface CartService {
    ShowCartDTO create(CartDTO cartDTO);
    ShowCartDTO update(long cart_id , CartDTO cartDTO);
    Boolean delete(long cart_id);
    PaginationDTO getAllPagination(int no, int limit);
}
