package com.seo.mobilestore.service;


import com.seo.mobilestore.data.dto.product.memory.MemoryDTO;
import com.seo.mobilestore.data.entity.Product;

import java.util.List;

public interface MemoryService {
    MemoryDTO create(MemoryDTO memoryDTO);

    List<MemoryDTO> createProductMemory(Product product, List<MemoryDTO> memoryDTOs);

    List<MemoryDTO> updateProductMemory(Product product, List<MemoryDTO> memoryDTOs);

}
