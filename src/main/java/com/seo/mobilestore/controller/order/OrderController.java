package com.seo.mobilestore.controller.order;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.constant.PageDefault;
import com.seo.mobilestore.data.dto.order.OrderCreationDTO;
import com.seo.mobilestore.data.dto.order.OrderUpdateDTO;
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
    public ResponseEntity<?> createOrder(@RequestBody OrderCreationDTO orderCreationDTO) {
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

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @Transactional
    @PutMapping("/{order_id}")
    public ResponseEntity<?> updatedOrders(@PathVariable long order_id, @RequestBody OrderUpdateDTO orderUpdateDTO) {
        return new ResponseEntity<>(this.orderService.update(order_id, orderUpdateDTO), HttpStatus.CREATED);
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @Transactional
    @DeleteMapping("/delete-order-by-user/{id}")
    public ResponseEntity<?> deleteOrderUser(@PathVariable long id) {

        this.orderService.deleteOrderByIdUser(id);

        return ResponseEntity.ok("DELETED");
    }

    /**
     * Method show list Order by User
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @GetMapping("/user")
    public ResponseEntity<?> showOrderByUser(@RequestParam(defaultValue = PageDefault.NO) int no,
                                             @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return new ResponseEntity<>(this.orderService.showOrderByUser(no, limit), HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @GetMapping("")
    public ResponseEntity<?> showAllOrderByAdmin(@RequestParam(defaultValue = PageDefault.NO) int no,
                                                 @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {

        return new ResponseEntity<>(this.orderService.showAllOrderByAdmin(no, limit), HttpStatus.OK);
    }

    /**
     * Method show details Order by User
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @Transactional
    @GetMapping("/user/detail/{orderId}")
    public ResponseEntity<?> showDetailsOrderByUser(@PathVariable long orderId) {

        return new ResponseEntity<>(this.orderService.getOrderDetailsDTO(orderId), HttpStatus.OK);
    }


    /**
     * Method show list Order by Admin
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Admin')")
    @Transactional
    @GetMapping("/detail/{orderId}")
    public ResponseEntity<?> showDetailsOrderByAdmin(@PathVariable long orderId) {

        return new ResponseEntity<>(this.orderService.getOrderDetailsDTO(orderId), HttpStatus.CREATED);
    }

}
