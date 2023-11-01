//package com.seo.mobilestore.controller.technical;
//
//import com.seo.mobilestore.constant.PageDefault;
//import com.seo.mobilestore.service.ProductTechService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import com.seo.mobilestore.constant.ApiURL;
//
//@RestController
//@RequestMapping(ApiURL.PRODUCT_TECHNICAL)
//public class ProductTechnicalController {
//
//    @Autowired
//    private ProductTechService productTechService;
//    @Autowired
//    private MessageSource messageSource;
//
//    @SecurityRequirement(name = "Bearer Authentication")
//    @PreAuthorize("hasAuthority('Role_Admin')")
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@PathVariable int id,
//                                    @RequestParam String info) {
//
//        return ResponseEntity.ok(productTechService.update(id, info));
//    }
//
//
//    @SecurityRequirement(name = "Bearer Authentication")
//    @PreAuthorize("hasAuthority('Role_Admin')")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteById(@PathVariable int id) {
//
//        this.productTechService.deleteById(id);
//
//        return ResponseEntity.ok(messageSource.getMessage("success.deleted", null, null));
//    }
//
//
//    @GetMapping("{id}")
//    public ResponseEntity<?> getProductTechnicalById(@PathVariable int id){
//        return ResponseEntity.ok(productTechService.getById(id));
//    }
//
//    @GetMapping("")
//    public ResponseEntity<?> getAllProductTechnicals(@RequestParam(defaultValue = PageDefault.NO) int no,
//                                         @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {
//
//        return new ResponseEntity<>(
//        		this.productTechService.getAllPagination(no, limit), HttpStatus.ACCEPTED);
//    }
//}
