package com.seo.mobilestore.controller;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.constant.PageDefault;
import com.seo.mobilestore.data.dto.cart.CartCreationDTO;
import com.seo.mobilestore.data.dto.cart.CartUpdateDTO;
import com.seo.mobilestore.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

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
    @Transactional
    @PostMapping(value = "")
    public ResponseEntity<?> createCart(@RequestBody CartCreationDTO cartCreationDTO) {

        return new ResponseEntity<>(this.cartService.create(cartCreationDTO), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @Transactional
    @PutMapping(value = "/{cart_id}")
    public ResponseEntity<?> updateCart(@PathVariable long cart_id , @RequestBody CartUpdateDTO cartUpdateDTO) {

        return new ResponseEntity<>(this.cartService.update(cart_id , cartUpdateDTO), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @DeleteMapping("/{product_id}")
    public ResponseEntity<?> deleteById(@PathVariable long product_id) {

        this.cartService.delete(product_id);
        return ResponseEntity.ok(messageSource.getMessage("success.deleted", null, null));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @GetMapping("/")
    public ResponseEntity<?> findAllCartOfUser(@RequestParam(defaultValue = PageDefault.NO) int no,
                                           @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return ResponseEntity.ok(cartService.getAllPagination(no, limit));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @DeleteMapping("/clear/{cart_id}")
    public ResponseEntity<?> clearCart(@PathVariable long cart_id) {

        this.cartService.clearCart(cart_id);
        return ResponseEntity.ok(messageSource.getMessage("success.deleted", null, null));
    }
}
