package com.seo.mobilestore.controller;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.constant.PageDefault;
import com.seo.mobilestore.data.dto.product.cart.CartDTO;
import com.seo.mobilestore.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiURL.CART)
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private MessageSource messageSource;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @PostMapping(value = "")
    public ResponseEntity<?> createCart(@RequestBody CartDTO cartDTO) {

        return new ResponseEntity<>(this.cartService.create(cartDTO), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @PutMapping(value = "/{cart_id}")
    public ResponseEntity<?> updateCart(@PathVariable long cart_id , @RequestBody CartDTO cartDTO) {

        return new ResponseEntity<>(this.cartService.update(cart_id , cartDTO), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @DeleteMapping("/{cart_id}")
    public ResponseEntity<?> deleteById(@PathVariable long cart_id) {

        this.cartService.delete(cart_id);

        return ResponseEntity.ok(messageSource.getMessage("success.deleted", null, null));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @GetMapping("/")
    public ResponseEntity<?> findAllCartOfUser(@RequestParam(defaultValue = PageDefault.NO) int no,
                                           @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return ResponseEntity.ok(cartService.getAllPagination(no, limit));
    }
}
