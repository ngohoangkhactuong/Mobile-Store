package com.seo.mobilestore.controller.order;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.data.dto.order.OrderCreationDTO;
import com.seo.mobilestore.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiURL.ORDER)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreationDTO orderCreationDTO){
        return ResponseEntity.ok(orderService.create(orderCreationDTO));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @Transactional
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrderAdmin(@PathVariable long orderId) {

        return new ResponseEntity<>(this.orderService.deleteOrder(orderId), HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @Transactional
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable long orderId) {

        return new ResponseEntity<>(this.orderService.cancelOrder(orderId), HttpStatus.OK);
    }

}
