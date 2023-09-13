package com.seo.mobilestore.service;

import com.seo.mobilestore.data.dto.product.color.ColorDTO;
import com.seo.mobilestore.data.entity.Product;
import java.util.List;

public interface ColorService {
    List<ColorDTO> createProductColor(Product product, List<ColorDTO> colorDTOs);
}
