package com.seo.mobilestore.controller.product.manufacturer;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.constant.PageDefault;
import com.seo.mobilestore.data.dto.product.manufacturer.ManufacturerDTO;
import com.seo.mobilestore.service.ManufacturerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

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

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @RequestBody ManufacturerDTO manufacturerDTO) {

        return ResponseEntity.ok(this.manufacturerService.update(id, manufacturerDTO));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        this.manufacturerService.delete(id);

        return ResponseEntity.ok(this.manufacturerService.delete(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getManufacturerById(@PathVariable long id) {
        return ResponseEntity.ok(manufacturerService.getById(id));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllManufacturer(@RequestParam(defaultValue = PageDefault.NO) int no,
                                                @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return new ResponseEntity<>(
                this.manufacturerService.getAllPagination(no, limit), HttpStatus.ACCEPTED);
    }
}
