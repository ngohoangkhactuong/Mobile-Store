package com.seo.mobilestore.controller.product.seri;


import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.constant.PageDefault;
import com.seo.mobilestore.data.dto.product.seri.SeriProductDTO;
import com.seo.mobilestore.service.SeriService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiURL.SERI)
public class SeriController {
    @Autowired
    private SeriService seriService;


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody SeriProductDTO seriDTO) {

        return ResponseEntity.ok(this.seriService.create(seriDTO));
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @PutMapping("/{seriID}")
    public ResponseEntity<?> update(
            @RequestBody SeriProductDTO seriProductDTO,
            @PathVariable long seriID) {

        return new ResponseEntity<>(this.seriService.update(seriID, seriProductDTO), HttpStatus.CREATED);
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        this.seriService.deletedByID(id);

        return ResponseEntity.ok("info.deleteSeri");
    }
    

    @GetMapping("{id}")
    public ResponseEntity<?> getSeriById(@PathVariable long id){
        return ResponseEntity.ok(seriService.getById(id));
    }
    
    @GetMapping("")
    public ResponseEntity<?> getAllPaymentMethods(@RequestParam(defaultValue = PageDefault.NO) int no,
                                         @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return new ResponseEntity<>(
        		this.seriService.getAllPagination(no, limit), HttpStatus.ACCEPTED);
    }
}
