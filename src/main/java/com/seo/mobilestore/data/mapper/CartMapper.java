package com.seo.mobilestore.data.mapper;

import com.seo.mobilestore.data.dto.product.cart.CartDTO;
import com.seo.mobilestore.data.dto.product.cart.ShowCartDTO;
import com.seo.mobilestore.data.entity.Cart;
import com.seo.mobilestore.data.mapper.product.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, UserMapper.class})
@Component
public interface CartMapper {

//    @Mapping(source = "user_id" , target = "user.id")
    @Mapping(source = "product_id", target = "product.id")
    Cart toEntity(CartDTO cartDTO);

    @Mapping(source = "user" , target = "userDTO")
    @Mapping(source = "product", target = "productDTO")
    @Mapping(source = "id", target = "cart_id")
    ShowCartDTO toShowDTO(Cart cart);
}
