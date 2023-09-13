package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
