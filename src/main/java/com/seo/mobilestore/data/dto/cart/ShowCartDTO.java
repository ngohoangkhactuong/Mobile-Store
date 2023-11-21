package com.seo.mobilestore.data.dto.cart;

import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import com.seo.mobilestore.data.dto.product.ShowProductOrderDTO;
import com.seo.mobilestore.data.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowCartDTO {
    private long cart_id;
    private UserDTO userDTO;
    private List<ShowProductOrderDTO> productOrderDTO;

}
