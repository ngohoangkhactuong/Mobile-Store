package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT rv FROM Review rv WHERE rv.product.id = :proId AND rv.status = true")
    Page<Review> findByProductIdCustomer(long proId, Pageable pageable);

    @Query("SELECT rv FROM Review rv WHERE rv.product.id = :proId AND rv.status = true")
    List<Review> findAllActiveByProductId(long proId);

    @Query("SELECT rv FROM Review rv WHERE rv.product.id = :proId")
    Page<Review> findByProductIdAdmin(long proId,  Pageable pageable);

    @Query("SELECT rv FROM Review rv")
    Page<Review> findAllByAdmin(Pageable pageable);

    @Query("SELECT rv FROM Review rv WHERE rv.status = true")
    Page<Review> findAllByCustomer(Pageable pageable);

}
