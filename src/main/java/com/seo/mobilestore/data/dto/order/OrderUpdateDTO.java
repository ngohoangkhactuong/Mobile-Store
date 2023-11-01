package com.seo.mobilestore.data.dto.order;

import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDTO {
    private long idPromotion;
    private String paymentMethodDTO;
    List<ProductOrderDTO> orderProductDTOList;
    List<Long> orderProductID;
    private long idAddress;
}

