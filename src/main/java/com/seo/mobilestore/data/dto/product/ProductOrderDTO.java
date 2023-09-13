package com.seo.mobilestore.data.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderDTO {
    private long id;
    private BigDecimal price;
    private String name;
    private String description;
    private String memory;
    private String color;
    private String seri;

}

