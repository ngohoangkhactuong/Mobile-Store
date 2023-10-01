package com.seo.mobilestore.data.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonIgnore
    private long id;
    private OrderDTO orderDTO;
    private int quantity;
    List<ProductOrderDTO> orderProductDTOList;

}
