package com.seo.mobilestore.data.mapper;

import com.seo.mobilestore.data.dto.order.OrderCreationDTO;
import com.seo.mobilestore.data.dto.order.OrderDTO;
import com.seo.mobilestore.data.dto.order.OrderDetailDTO;
import com.seo.mobilestore.data.entity.OrderDetails;
import com.seo.mobilestore.data.entity.Orders;
import com.seo.mobilestore.data.mapper.paymentMethod.PaymentMethodMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {  PaymentMethodMapper.class,StatusMapper.class, UserMapper.class})
@Component

public interface OrderMapper {

    @Mapping(source = "total", target = "total")
    @Mapping(source = "receiveDate", target = "receiveDate")
    @Mapping(source = "user", target = "userDTO")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "paymentMethod", target = "paymentMethodDTO")
    @Mapping(source = "status", target = "statusDTO")
    OrderDTO toDTO(Orders orders);

    @Mapping(source = "statusDTO", target = "status")
    Orders toEnity(OrderCreationDTO orderCreationDTO);

    @Mapping(source = "orderDTO", target = "orders")
    @Mapping(ignore = true, target = "quantity")
    @Mapping(ignore = true, target = "memory")
    @Mapping(ignore = true, target = "seri")
    @Mapping(ignore = true, target = "color")
    OrderDetails toDetailEnity(OrderDetailDTO orderDetailsDTO);

}
