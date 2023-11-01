package com.seo.mobilestore.service.impl;

import com.seo.mobilestore.data.dto.product.memory.MemoryDTO;
import com.seo.mobilestore.data.dto.product.memory.MemoryProductDTO;
import com.seo.mobilestore.data.entity.Memory;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.mapper.product.MemoryMapper;
import com.seo.mobilestore.data.repository.MemoryRepository;
import com.seo.mobilestore.service.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class MemoryServiceImpl implements MemoryService {

    @Autowired
    private MemoryRepository memoryRepository;
    @Autowired
    private MemoryMapper memoryMapper;

    @Override
    public MemoryDTO create(MemoryDTO memoryDTO) {
        return memoryMapper.toDTO(memoryRepository.save(memoryMapper.toEntity(memoryDTO)));
    }


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

    @Override
    public List<MemoryProductDTO> createProductMemoryCreation(Product product, List<MemoryProductDTO> memoryDTOs){

        List<MemoryProductDTO> memoryDTOsRessult = new ArrayList<>();

        memoryDTOs.forEach(memoryProductDTO -> {

            Memory memory = memoryMapper.toMemoryEntity(memoryProductDTO);
            memory.setProduct(product);

            memoryDTOsRessult.add(memoryMapper.toMemoryDTO(memoryRepository.save(memory)));
        });
        return memoryDTOsRessult;
    }

    @Override
    public List<MemoryProductDTO> updateProductMemory(Product product, List<MemoryProductDTO> memoryDTOs){

        List<MemoryProductDTO> memoryDTOsResult = new ArrayList<>();
        List<Memory> memorys = memoryRepository.findByProductId(product.getId());
        List<MemoryProductDTO> memoryDTOsPrepare = new ArrayList<>();

        memorys.forEach(memory -> {
            memoryDTOsPrepare.add(memoryMapper.toMemoryDTO(memory));
            if (!memoryDTOs.contains(memoryMapper.toMemoryDTO(memory))){
                // Update status = false (0) for memory when consumer delete this memory
                memory.setStatus(false);
                memoryRepository.save(memory);
            } else {
                memoryDTOs.forEach(memoryDTO -> {
                    // if memory existed in database, it will be updated then save it
                    if (Objects.equals(memoryDTO.getName(), memory.getName())) {
                        Memory memoryMapped=  memoryMapper.toMemoryEntity(memoryDTO);
                        memoryMapped.setProduct(product);
                        memoryDTOsResult.add(memoryMapper.toMemoryDTO(memoryRepository.save(memoryMapped)));
                    }
                });
            }
        });
        // a foreach to check a memoryDTO in list memoryDTO have the new memory, yes or no?
        // if existing new memory then save this memory into database
        memoryDTOs.forEach(memoryDTO -> {
            if (!memoryDTOsPrepare.contains(memoryDTO)) {
                Memory memoryMapped=  memoryMapper.toMemoryEntity(memoryDTO);
                memoryMapped.setProduct(product);
                Memory memorySaved = memoryRepository.save(memoryMapped);
                memoryDTOsResult.add(memoryMapper.toMemoryDTO(memorySaved));
            }
        });

        return memoryDTOsResult;
    }

}
