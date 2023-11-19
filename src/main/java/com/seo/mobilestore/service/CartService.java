package com.seo.mobilestore.service;

import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.cart.CartCreationDTO;
import com.seo.mobilestore.data.dto.product.cart.CartDetailDTO;
import com.seo.mobilestore.data.dto.product.cart.ShowCartDTO;

public interface CartService {
    CartDetailDTO create(CartCreationDTO cartCreationDTO);
    ShowCartDTO update(long cart_id , CartCreationDTO cartCreationDTO);
    Boolean delete(long cart_id);
    PaginationDTO getAllPagination(int no, int limit);
}
