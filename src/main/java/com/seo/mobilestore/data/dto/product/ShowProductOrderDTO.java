package com.seo.mobilestore.data.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowProductOrderDTO extends ProductOrderDTO {
    private String image;
}
