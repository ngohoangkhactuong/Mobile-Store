package com.seo.mobilestore.service.product;

import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.FilterProductDTO;
import com.seo.mobilestore.data.dto.product.ProductCreationDTO;
import com.seo.mobilestore.data.dto.product.ProductDTO;
import com.seo.mobilestore.data.dto.product.ShowProductRelated;
import com.seo.mobilestore.data.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductCreationDTO create(ProductCreationDTO productCreationDTO, List<MultipartFile> fileImages);

//    ProductDTO update(long id, ProductDTO productDTO, List<MultipartFile> fileImages);
    ProductCreationDTO update(long id, ProductCreationDTO productCreationDTO, List<MultipartFile> fileImages);
    PaginationDTO findAllActiveProduct(int no, int limit);

    PaginationDTO searchProduct(String keyword, int no, int limit);

    boolean deleteById(long id);

    List<ProductDTO>  findNewProduct();

    PaginationDTO showListProductPagination(long categoryId , int no , int limit);

    ProductDTO showDetailProduct(long id);

    PaginationDTO showListProductFilter(FilterProductDTO filterProductDTO, int no, int limit);
    PaginationDTO showProductByCategoryAndManufacturer(long category_id , long manufacturer_id, int no, int limit);

    List<ShowProductRelated> showRelatedProduct(long productId, int quantity);

    PaginationDTO showActiveProductByCategory(long id, int no, int limit);

    PaginationDTO showActiveProductByManufacturer(long id, int no, int limit);
    PaginationDTO FilterProductsByPrice(BigDecimal lowerPrice, BigDecimal higherPrice, int no, int limit);

}
