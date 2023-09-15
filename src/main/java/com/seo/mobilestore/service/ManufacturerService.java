package com.seo.mobilestore.service;

import com.seo.mobilestore.common.MessageResponse;
import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.product.ProductDTO;
import com.seo.mobilestore.data.dto.product.manufacturer.ManufacturerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ManufacturerService {
    MessageResponse create(ManufacturerDTO manufacturerDTO);

    ManufacturerDTO update(long id , ManufacturerDTO manufacturerDTO);

    Boolean delete(long id);

    ManufacturerDTO getById(long id);

    PaginationDTO getAllPagination(int no, int limit);
}
