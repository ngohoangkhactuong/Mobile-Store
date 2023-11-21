package com.seo.mobilestore.data.dto.product;

import com.seo.mobilestore.data.dto.*;
import com.seo.mobilestore.data.dto.product.categories.CategoriesDTO;
import com.seo.mobilestore.data.dto.product.manufacturer.ManufacturerDTO;
import com.seo.mobilestore.data.dto.product.memory.MemoryDTO;
import com.seo.mobilestore.data.dto.product.review.ReviewDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private long id;
    private CategoriesDTO categoriesDTO;
    private ManufacturerDTO manufacturerDTO;
    private String name;
    private String description;
    private BigDecimal price;
    private int stocks;
    private boolean status;
    private int views;
    private float star;
//    private List<ProductTechDTO> productTechDTOs;
    private List<SeriDTO> seriDTOs;
    private List<MemoryDTO> memoryDTOs;
    private List<ReviewDTO> reviewDTOs;
    private List<ImageDTO> imageDTOs;
}
