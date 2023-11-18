package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.product.id = :product_id")
    Cart findByProductId(long product_id);

    @Query("SELECT c FROM Cart c WHERE c.user.id = :user_id")
    Page<Cart> findByUserId(long user_id , Pageable pageable);
}
