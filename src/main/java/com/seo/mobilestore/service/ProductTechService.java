package com.seo.mobilestore.service;


import com.seo.mobilestore.data.dto.product.ProductTechDTO;
import com.seo.mobilestore.data.entity.Product;

import java.util.List;

public interface ProductTechService {
    List<ProductTechDTO> createProductTech(Product product, List<ProductTechDTO> productTechDTOs);
}
