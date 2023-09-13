package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.ProductTech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTechRepository extends JpaRepository<ProductTech , Long> {
}
