package com.seo.mobilestore.controller.address;

import com.seo.mobilestore.constant.ApiURL;
import com.seo.mobilestore.data.dto.address.AddressDTO;
import com.seo.mobilestore.service.AddressService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiURL.ADDRESS)
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private MessageSource messageSource;


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdAddress(@PathVariable long id){
        return ResponseEntity.ok(addressService.getById(id));
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize(value = "isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<?> getAllAddress(){
        return ResponseEntity.ok(addressService.getAllAddress());
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @PostMapping("")
    public ResponseEntity<?> createAddress(@RequestBody AddressDTO addressDTO) {

        return new ResponseEntity<>(this.addressService.createAddress(addressDTO), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @PutMapping("/update-address/{id}")
    public ResponseEntity<?> updateAddress(@RequestBody AddressDTO addressDTO, @PathVariable long id) {

        return ResponseEntity.ok(addressService.updateAddress(addressDTO, id));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('Role_Customer')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {

        this.addressService.deleteAddress(id);

        return ResponseEntity.ok(messageSource.getMessage("success.deleted", null, null));
    }
}
