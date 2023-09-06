package com.seo.mobilestore.data.mapper;

import com.seo.mobilestore.data.dto.StatusDTO;
import com.seo.mobilestore.data.entity.Status;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface StatusMapper {

    Status toEntity(StatusDTO statusDTO);

    StatusDTO toDTO(Status status);
}
