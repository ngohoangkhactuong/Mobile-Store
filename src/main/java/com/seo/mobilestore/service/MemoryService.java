package com.seo.mobilestore.service;


import com.seo.mobilestore.data.dto.product.memory.MemoryDTO;
import com.seo.mobilestore.data.entity.Product;

import java.util.List;

public interface MemoryService {
    List<MemoryDTO> createProductMemory(Product product, List<MemoryDTO> memoryDTOs);


}
