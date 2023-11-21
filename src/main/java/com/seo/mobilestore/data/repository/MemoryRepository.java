package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Memory;
import com.seo.mobilestore.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoryRepository extends JpaRepository<Memory  , Long> {
    @Query(value = "SELECT m FROM Memory m WHERE m.product.id =:#{#product.id} AND m.name =:name")
    boolean findByNameAndProductId(String name, @Param("product") Product product);

    @Query(value = "SELECT m FROM Memory m WHERE m.product.id =:id and m.status= true")
    List<Memory> findByProductId(long id);

    @Query("SELECT m FROM Memory m WHERE m.name = :memoryName AND m.product.name = :productName")
    Optional<Memory> findMemoryByNameAndProductName(@Param("memoryName") String memoryName, @Param("productName") String productName);
}
