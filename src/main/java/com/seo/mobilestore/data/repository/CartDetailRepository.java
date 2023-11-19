package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetails , Long> {
}
