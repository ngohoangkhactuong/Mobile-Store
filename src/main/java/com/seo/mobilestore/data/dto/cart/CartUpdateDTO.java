package com.seo.mobilestore.data.dto.cart;

import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartUpdateDTO {
    List<ProductOrderDTO> orderProductDTOList;
}
