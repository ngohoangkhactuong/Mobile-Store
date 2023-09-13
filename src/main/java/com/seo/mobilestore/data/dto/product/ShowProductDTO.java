package com.seo.mobilestore.data.dto.product;

import com.seo.mobilestore.data.dto.ImageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowProductDTO extends ProductDetailsDTO {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private List<ImageDTO> imageDTOs;
}

