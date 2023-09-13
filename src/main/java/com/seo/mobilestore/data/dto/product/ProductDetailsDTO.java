package com.seo.mobilestore.data.dto.product;

import com.seo.mobilestore.data.dto.product.categories.CategoriesDTO;
import com.seo.mobilestore.data.dto.product.manufacturer.ManufacturerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDTO {
    private CategoriesDTO categoriesDTO;
    private ManufacturerDTO manufacturerDTO;
    private int stocks;
    private boolean status;
}

