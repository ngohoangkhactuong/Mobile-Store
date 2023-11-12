package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    boolean existsByPromotionId(long promotionId);

    @Query(value = "SELECT a FROM Orders a WHERE a.user.id = :userId AND a.id = :orderId")
    Orders findOrderByUserId(long userId, long orderId);

    @Query(value = "SELECT a FROM Orders a WHERE a.user.id = :id ORDER BY a.id DESC")
    Page<Orders> findAllByUserId(long id, Pageable pageable);

    @Query(value = "SELECT a FROM Orders a ORDER BY a.id DESC")
    Page<Orders> findAll(Pageable pageable);


}
