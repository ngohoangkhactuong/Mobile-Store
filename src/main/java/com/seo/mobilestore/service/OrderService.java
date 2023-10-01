package com.seo.mobilestore.service;

import com.seo.mobilestore.data.dto.order.OrderCreationDTO;
import com.seo.mobilestore.data.dto.order.OrderDTO;
import com.seo.mobilestore.data.dto.order.OrderDetailDTO;

import java.math.BigDecimal;

public interface OrderService {
    OrderDetailDTO create(OrderCreationDTO orderCreationDTO);
    BigDecimal calculateDiscountedTotal(BigDecimal total, int discountPercentage, BigDecimal totalPurchase, BigDecimal maxDiscountAmount);

    Boolean deleteOrderByCustomer(long orderId);

    Boolean cancelOrder(long idOrder);

    Boolean deleteOrder(long id);
}
