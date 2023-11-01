//package com.seo.mobilestore.controller.technical;
//
//import com.seo.mobilestore.constant.ApiURL;
//import com.seo.mobilestore.constant.PageDefault;
//import com.seo.mobilestore.data.dto.TechnicalDTO;
//import com.seo.mobilestore.service.TechnicalService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(ApiURL.TECHNICAL)
//public class TechnicalController {
//
//    @Autowired
//    private TechnicalService technicalService;
//
//    @SecurityRequirement(name = "Bearer Authentication")
//    @PreAuthorize("hasAuthority('Role_Admin')")
//    @PostMapping("")
//    public ResponseEntity<?> create(@RequestParam String name){
//        return ResponseEntity.ok(technicalService.create(name));
//    }
//
//    @SecurityRequirement(name = "Bearer Authentication")
//    @PreAuthorize("hasAuthority('Role_Admin')")
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@PathVariable int id,
//                                    @RequestBody TechnicalDTO technicalDTO) {
//
//        return ResponseEntity.ok(this.technicalService.update(id, technicalDTO));
//    }
//
//    @GetMapping("{id}")
//    public ResponseEntity<?> getTechnicalById(@PathVariable int id){
//        return ResponseEntity.ok(technicalService.getById(id));
//    }
//
//    @GetMapping("")
//    public ResponseEntity<?> getAllTechnicals(@RequestParam(defaultValue = PageDefault.NO) int no,
//                                         @RequestParam(defaultValue = PageDefault.LIMIT) int limit) {
//
//        return new ResponseEntity<>(
//        		this.technicalService.getAllPagination(no, limit), HttpStatus.ACCEPTED);
//    }
//
//}
