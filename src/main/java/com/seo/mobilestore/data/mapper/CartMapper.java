package com.seo.mobilestore.data.mapper;

import com.seo.mobilestore.data.dto.cart.*;
import com.seo.mobilestore.data.entity.Cart;
import com.seo.mobilestore.data.entity.CartDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
@Component
public interface CartMapper {

    Cart toEntity(CartCreationDTO cartCreationDTO);
    @Mapping(source = "user", target = "userDTO")
    CartDTO toDTO(Cart cart);

    @Mapping(source = "user" , target = "userDTO")
    @Mapping(source = "id" , target = "cart_id")
    ShowCartDTO toShowDTO(Cart cart);

    @Mapping(source = "cartDTO", target = "cart")
    @Mapping(ignore = true, target = "quantity")
    @Mapping(ignore = true, target = "memory")
    @Mapping(ignore = true, target = "seri")
    @Mapping(ignore = true, target = "color")
    CartDetails toDetailEntity(CartDetailDTO cartDetailDTO);

    Cart toCartUpdateEntity(CartUpdateDTO cartUpdateDTO);
}
