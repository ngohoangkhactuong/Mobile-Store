package com.seo.mobilestore.data.dto.cart;

import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import com.seo.mobilestore.data.dto.product.ShowProductOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowCartDetailDTO {
    private long id;
    private CartDTO cartDTO;
    private int quantity;
    List<ShowProductOrderDTO> orderProductDTOList;
}
