package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetails , Long> {

    @Query("SELECT cd FROM CartDetails cd WHERE cd.cart.id = :cart_id")
    List<CartDetails> findAllByCartId(long cart_id);
}
