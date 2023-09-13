package com.seo.mobilestore.data.dto.product;

import com.seo.mobilestore.data.dto.ImageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShowProductRelated {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private List<ImageDTO> imageDTOs;
}

