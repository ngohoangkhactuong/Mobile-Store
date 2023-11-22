package com.seo.mobilestore.service;

import com.seo.mobilestore.data.dto.PaginationDTO;
import com.seo.mobilestore.data.dto.order.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;

public interface OrderService {
    OrderDetailDTO create(OrderCreationDTO orderCreationDTO);

    BigDecimal totalProduct(BigDecimal total, int discount, BigDecimal totalPurchase, BigDecimal maxGet);

    boolean deleteOrderByIdUser(long id);

//    @Transactional
//    OrderDetailDTO update(long order_id, OrderUpdateDTO orderUpdateDTO);

    PaginationDTO showOrderByUser(int no, int limit);

    ShowOrderDetailsDTO getOrderDetailsDTO(long orderId);

    PaginationDTO showAllOrderByAdmin(int no, int limit);

    Boolean deleteOrder(long id);

    Boolean deleteOrderByCustomer(long orderId);

    Boolean cancelOrder(long idOrder);

}
