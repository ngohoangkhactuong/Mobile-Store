package com.seo.mobilestore.service;


import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriProductCreationDTO;
import com.seo.mobilestore.data.dto.product.seri.SeriProductDTO;
import com.seo.mobilestore.data.entity.Product;

import java.util.List;

public interface SeriService {


    List<SeriDTO> createProductSeri(Product product, List<SeriDTO> seriDTOs);

    List<SeriProductCreationDTO> createProductSeriCreation(Product product, List<SeriProductCreationDTO> seriDTOs);
//    List<SeriDTO> updateProductSeri(Product product, List<SeriDTO> seriDTOs);

    List<SeriProductCreationDTO> updateProductSeri(Product product, List<SeriProductCreationDTO> seriDTOs);
    MessageResponse create(SeriProductDTO seriDTO);

    boolean deletedByID(long id);

    SeriProductDTO update(long seriID , SeriProductDTO seriProductDTO);

    SeriDTO getById(long id);

    PaginationDTO getAllPagination(int no, int limit);


}
