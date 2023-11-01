package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {
    @Query(value = "SELECT od FROM OrderDetails od" +
            " WHERE od.orders.id = :orderId")
    List<OrderDetails> findAllByOrderId(long orderId);

    @Query(value = "SELECT count(*) FROM OrderDetails od" +
            " WHERE od.orders.id = :orderId and od.orders.status = 1")
    long CountByOrderId(long orderId);

    @Query(value = "SELECT od FROM OrderDetails od" +
            " WHERE od.orders.id = :orderId and od.orders.user.id = :userId and od.orders.status = 1")
    List<OrderDetails> findAllByOrderIdAndUserId(long orderId, long userId);

    @Query(value = "SELECT od FROM OrderDetails od" +
            " WHERE od.orders.id = :order_id")
    OrderDetails findByOrderId(long order_id);

    @Query("SELECT od FROM  OrderDetails  od WHERE od.address.id = :addressId")
    List<OrderDetails> existsAddress(long addressId);


}
