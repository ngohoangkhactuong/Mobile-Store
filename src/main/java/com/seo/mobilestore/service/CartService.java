package com.seo.mobilestore.service;

import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.cart.CartCreationDTO;
import com.seo.mobilestore.data.dto.cart.CartDetailDTO;
import com.seo.mobilestore.data.dto.cart.CartUpdateDTO;
import com.seo.mobilestore.data.dto.cart.ShowCartDTO;

public interface CartService {
    CartDetailDTO create(CartCreationDTO cartCreationDTO);
    CartDetailDTO update(long cart_id , CartUpdateDTO cartUpdateDTO);
    Boolean delete(long cart_id);
    PaginationDTO getAllPagination(int no, int limit);
}
