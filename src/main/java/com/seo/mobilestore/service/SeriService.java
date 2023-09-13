package com.seo.mobilestore.service;


import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriProductDTO;
import com.seo.mobilestore.data.entity.Product;

import java.util.List;

public interface SeriService {


    List<SeriDTO> createProductSeri(Product product, List<SeriDTO> seriDTOs);

}