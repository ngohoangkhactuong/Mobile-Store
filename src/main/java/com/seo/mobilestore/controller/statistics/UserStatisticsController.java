package com.seo.mobilestore.controller.statistics;

import com.seo.mobilestore.constant.ApiURL;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 @CrossOrigin(origins = "*", maxAge = 3600)
 @RestController
 @RequestMapping(ApiURL.USER_STATISTIC)
 @SecurityRequirement(name = "Bearer Authentication")
 @PreAuthorize(value = "hasRole('Admin')")
 public class UserStatisticsController {

 }
