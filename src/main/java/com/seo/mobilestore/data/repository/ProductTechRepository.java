package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.dto.TechnicalDTO;
import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.entity.ProductTech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTechRepository extends JpaRepository<ProductTech , Long> {
    @Query(value = "SELECT i FROM ProductTech i " +
            "WHERE i.product.id =:#{#product.id} " +
            "AND i.info =:#{#technical.info} " +
            "AND i.technical.id=:#{#technical.id}")
    boolean findByProductIdAndInfoAndTechnicalId(@Param("technical") TechnicalDTO technicalDTO, @Param("product") Product product);

    @Query(value = "SELECT pt FROM ProductTech pt WHERE pt.product.id =:id and pt.status= true")
    List<ProductTech> findByProductId(long id);

    Optional<ProductTech> findById(int id);

}
