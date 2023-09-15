package com.seo.mobilestore.controller.product.promotion;


import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.constant.PageDefault;
import com.seo.mobilestore.data.dto.promotion.PromotionCreationDTO;
import com.seo.mobilestore.data.dto.promotion.PromotionDTO;
import com.seo.mobilestore.service.PromotionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiURL.PROMOTION)
public class PromotionController {

    @Autowired
    private PromotionService promotionService;
    @Autowired
    private MessageSource messageSource;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody PromotionCreationDTO promotionCreationDTO) {

        return new ResponseEntity<>(this.promotionService.create(promotionCreationDTO), HttpStatus.CREATED);
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @RequestBody PromotionCreationDTO promotionCreationDTO) {

        PromotionDTO promotionDTO = this.promotionService.update(id, promotionCreationDTO);

        return ResponseEntity.ok(promotionDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {

        return ResponseEntity.ok(this.promotionService.getById(id));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('Role_Admin','Role_Customer')")
    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = PageDefault.NO) int no,
                                    @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return ResponseEntity.ok(this.promotionService.getAll(no, limit));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @GetMapping("/code/{discountCode}")
    public ResponseEntity<?> getAllByDiscountCode(@PathVariable String discountCode,
                                                  @RequestParam(defaultValue = PageDefault.NO) int no,
                                                  @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return ResponseEntity.ok(this.promotionService.findAllByDiscountCode(discountCode, no, limit));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {

        this.promotionService.deleteById(id);

        return ResponseEntity.ok(messageSource.getMessage("success.deleted", null, null));
    }
}
