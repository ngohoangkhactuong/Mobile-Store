package com.seo.mobilestore.data.dto.product.cart;

import com.seo.mobilestore.data.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private long id;
    private UserDTO userDTO;
}
