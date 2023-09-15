package com.seo.mobilestore.controller.product.manufacturer;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.data.dto.product.ProductDTO;
import com.seo.mobilestore.data.dto.product.manufacturer.ManufacturerDTO;
import com.seo.mobilestore.service.ManufacturerService;
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
@RequestMapping(ApiURL.MANUFACTURER)
public class ManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PostMapping(value = "")
    public ResponseEntity<?> createManufacturer(@Valid @RequestBody ManufacturerDTO manufacturerDTO) {
        return new ResponseEntity<>(this.manufacturerService.create(manufacturerDTO), HttpStatus.CREATED);
    }
}
