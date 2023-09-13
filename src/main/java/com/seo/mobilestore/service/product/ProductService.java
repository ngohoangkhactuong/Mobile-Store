package com.seo.mobilestore.service.product;

import com.seo.mobilestore.data.dto.product.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDTO create(ProductDTO productDTO, List<MultipartFile> fileImages);
}
