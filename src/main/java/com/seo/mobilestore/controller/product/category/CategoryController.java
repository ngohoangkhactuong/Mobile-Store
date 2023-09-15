package com.seo.mobilestore.controller.product.category;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.constant.PageDefault;
import com.seo.mobilestore.data.dto.product.categories.CategoriesDTO;
import com.seo.mobilestore.service.CategoriesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiURL.CATEGORIES)
public class CategoryController {
    @Autowired
    private CategoriesService categoriesService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody CategoriesDTO categoriesDTO){
        return new ResponseEntity<>(this.categoriesService.create(categoriesDTO), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @RequestBody CategoriesDTO categoriesDTO) {

        return ResponseEntity.ok(this.categoriesService.update(id, categoriesDTO));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        this.categoriesService.delete(id);

        return ResponseEntity.ok(this.categoriesService.delete(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoriesById(@PathVariable long id) {
        return ResponseEntity.ok(categoriesService.getById(id));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = PageDefault.NO) int no,
                                              @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return new ResponseEntity<>(
                this.categoriesService.getAllPagination(no, limit), HttpStatus.ACCEPTED);
    }

}
