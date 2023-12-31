package com.seo.mobilestore.data.mapper.product;

import com.seo.mobilestore.data.dto.product.memory.MemoryDTO;
import com.seo.mobilestore.data.dto.product.memory.MemoryProductDTO;
import com.seo.mobilestore.data.entity.Memory;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface MemoryMapper {

    Memory toEntity(MemoryDTO memoryDTO);

    Memory toMemoryEntity(MemoryProductDTO memoryProductDTO);

    MemoryDTO toDTO(Memory memory);

    MemoryProductDTO toMemoryDTO(Memory memory);
}

