package com.seo.mobilestore.controller.product;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.data.dto.product.ProductDTO;
import com.seo.mobilestore.service.product.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ApiURL.PRODUCT)
public class ProductController {

    @Autowired
    private ProductService productService;


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createProduct(@Valid @RequestPart ProductDTO productDTO,
                                           @RequestPart(name = "fileImages", required = false)
                                           List<MultipartFile> fileImages) {

        return new ResponseEntity<>(this.productService.create(productDTO, fileImages), HttpStatus.CREATED);
    }

}
