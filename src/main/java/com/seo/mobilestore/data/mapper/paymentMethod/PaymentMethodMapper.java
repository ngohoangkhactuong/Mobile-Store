package com.seo.mobilestore.data.mapper.paymentMethod;

import com.seo.mobilestore.data.dto.paymentMethod.PaymentMethodDTO;
import com.seo.mobilestore.data.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    @Mapping(target = "status",constant = "true")
    PaymentMethod toEnity(PaymentMethodDTO paymentMethodDTO);

    PaymentMethodDTO toDTO(PaymentMethod paymentMethod);
}
