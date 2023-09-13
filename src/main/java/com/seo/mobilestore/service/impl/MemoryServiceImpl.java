package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.data.dto.product.memory.MemoryDTO;
import com.seo.mobilestore.data.entity.Memory;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.mapper.product.MemoryMapper;
import com.seo.mobilestore.data.repository.MemoryRepository;
import com.seo.mobilestore.service.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class MemoryServiceImpl implements MemoryService {

    @Autowired
    private MemoryRepository memoryRepository;
    @Autowired
    private MemoryMapper memoryMapper;

    @Override
    public List<MemoryDTO> createProductMemory(Product product, List<MemoryDTO> memoryDTOs){

        List<MemoryDTO> memoryDTOsRessult = new ArrayList<>();

        memoryDTOs.forEach(memoryDTO -> {

            Memory memory = memoryMapper.toEntity(memoryDTO);
            memory.setProduct(product);

            memoryDTOsRessult.add(memoryMapper.toDTO(memoryRepository.save(memory)));
        });
        return memoryDTOsRessult;
    }


}
