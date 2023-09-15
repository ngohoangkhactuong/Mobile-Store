package com.seo.mobilestore.controller.product.color;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.data.dto.product.color.ColorProductDTO;
import com.seo.mobilestore.service.ColorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiURL.COLOR)
public class ColorController {
    @Autowired
    private ColorService colorService;
    @Autowired
    private MessageSource messageSource;


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody ColorProductDTO colorDTO ){
        return ResponseEntity.ok(colorService.create(colorDTO));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @RequestBody ColorProductDTO colorDTO) {

        return ResponseEntity.ok(colorService.update(id, colorDTO));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        this.colorService.delete(id);
        return ResponseEntity.ok(messageSource.getMessage("success.deleted", null, null));
    }

}
