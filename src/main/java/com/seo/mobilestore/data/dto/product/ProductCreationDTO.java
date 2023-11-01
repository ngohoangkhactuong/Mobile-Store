package com.seo.mobilestore.data.dto.product;

import com.seo.mobilestore.data.dto.ImageDTO;
import com.seo.mobilestore.data.dto.product.categories.CategoriesDTO;
import com.seo.mobilestore.data.dto.product.manufacturer.ManufacturerDTO;
import com.seo.mobilestore.data.dto.product.memory.MemoryProductDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriProductCreationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreationDTO {
    private CategoriesDTO categoriesDTO;
    private ManufacturerDTO manufacturerDTO;
    private String name;
    private String description;
    private BigDecimal price;
    private int stocks;
    private boolean status;
    private int views;
    private float star;
    private List<SeriProductCreationDTO> seriDTOs;
    private List<MemoryProductDTO> memoryDTOs;
    private List<ImageDTO> imageDTOs;
}
