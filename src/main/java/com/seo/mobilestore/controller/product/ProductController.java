package com.seo.mobilestore.controller.product;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.constant.PageDefault;
import com.seo.mobilestore.constant.PriceDefault;
import com.seo.mobilestore.data.dto.product.FilterProductDTO;
import com.seo.mobilestore.data.dto.product.ProductCreationDTO;
import com.seo.mobilestore.service.product.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(ApiURL.PRODUCT)
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private MessageSource messageSource;


    /**
     * Method get new product
     */
    @GetMapping("/new")
    public ResponseEntity<?> getNewProduct() {
        return ResponseEntity.ok(this.productService.findNewProduct());
    }

    /**
     * Method create products
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @Transactional
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createProduct(@Valid @RequestPart ProductCreationDTO productCreationDTO,
                                           @RequestPart(name = "fileImages", required = false)
                                           List<MultipartFile> fileImages) {

        return new ResponseEntity<>(this.productService.create(productCreationDTO, fileImages), HttpStatus.CREATED);
    }

    /**
     * Method update products
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @Transactional
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateProduct(@PathVariable long id, @Valid @RequestPart ProductCreationDTO productCreationDTO,
                                           @RequestPart(name = "fileImages", required = false)
                                           List<MultipartFile> fileImages) {

        return new ResponseEntity<>(this.productService.update(id, productCreationDTO, fileImages), HttpStatus.CREATED);
    }

    /**
     * method Search product following the page
     */
    @GetMapping("/active")
    public ResponseEntity<?> findAllActive(@RequestParam(defaultValue = PageDefault.NO) int no,
                                           @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return ResponseEntity.ok(productService.findAllActiveProduct(no, limit));
    }

    /**
     * Method Search product by keyword
     */
//    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/search-product")
    public ResponseEntity<?> searchProduct(@RequestParam(defaultValue = "") String keyword,
                                           @RequestParam(defaultValue = PageDefault.NO) int no,
                                           @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return ResponseEntity.ok().body(this.productService.searchProduct(keyword, no, limit));
    }


    /**
     * Method delete product by status
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {

        this.productService.deleteById(id);

        return ResponseEntity.ok(messageSource.getMessage("success.deleted", null, null));
    }

    /**
     * Method show product by categories
     */
//    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/show-product/{categoryId}")
    public ResponseEntity<?> showListProduct(@PathVariable Long categoryId,
                                             @RequestParam(defaultValue = PageDefault.NO) int no,
                                             @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return ResponseEntity.ok().body(this.productService.showListProductPagination(categoryId, no, limit));
    }

    /**
     * Method show detail and increase view
     */
//    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> showProductDetail(@PathVariable long id) {

        return ResponseEntity.ok().body(this.productService.showDetailProduct(id));
    }


    /**
     * method search products by many params
     */
//    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/filter-product")
    public ResponseEntity<?> showListProduct(
            @RequestParam int manufacturerId,
            @RequestParam int categoryId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = PriceDefault.LOWER_PRICE) BigDecimal lowerPrice,
            @RequestParam(defaultValue = PriceDefault.HIGHER_PRICE) BigDecimal higherPrice,
            @RequestParam(defaultValue = PageDefault.NO) int no,
            @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {
        if (lowerPrice == null){
            lowerPrice = new BigDecimal(PriceDefault.LOWER_PRICE);
        }
        if (higherPrice == null)
        {
            higherPrice = new BigDecimal(PriceDefault.HIGHER_PRICE);
        }

        return ResponseEntity.ok().body(this.productService
                .showListProductFilter(
                        manufacturerId,
                        categoryId,
                        keyword,
                        lowerPrice,
                        higherPrice,
                        no,
                        limit));
    }


    /**
     * Method related products by category and manufacturer (default = 6)
     */

    @PreAuthorize("hasAuthority('Role_Customer')")
    @GetMapping("/related-product")
    public ResponseEntity<?> showRelatedProduct(@RequestParam(defaultValue = "0") long productId,
                                                @RequestParam(defaultValue = "6") int quantity
    ) {
        return ResponseEntity.ok().body(this.productService.showRelatedProduct(productId, quantity));
    }

    /**
     * Method show active product by category
     */
//    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/active-category/{categoryId}")
    public ResponseEntity<?> showActiveProductByCategory(@PathVariable long categoryId,
                                                         @RequestParam(defaultValue = PageDefault.NO) int no,
                                                         @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {
        return ResponseEntity.ok().body(this.productService.showActiveProductByCategory(categoryId, no, limit));
    }


    /**
     * Method show active product by manufacturer
     */
//    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/active-manufacturer/{manufacturerId}")
    public ResponseEntity<?> showActiveProductByManufacturer(@PathVariable long manufacturerId,
                                                             @RequestParam(defaultValue = PageDefault.NO) int no,
                                                             @RequestParam(defaultValue = PageDefault.LIMIT)
                                                             int limit) {
        return ResponseEntity.ok().body(this.productService.showActiveProductByManufacturer(manufacturerId, no, limit));
    }

    /**
     * Show active product by price
     */
//    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/active-price")
    public ResponseEntity<?> showActiveProductByPrice(@RequestParam(defaultValue = PriceDefault.LOWER_PRICE)
                                                          BigDecimal lowerPrice,
                                                      @RequestParam(defaultValue = PriceDefault.HIGHER_PRICE)
                                                      BigDecimal higherPrice,
                                                      @RequestParam(defaultValue = PageDefault.NO) int no,
                                                      @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {
        return ResponseEntity.ok().body(this.productService.FilterProductsByPrice(lowerPrice, higherPrice, no, limit));
    }
}
