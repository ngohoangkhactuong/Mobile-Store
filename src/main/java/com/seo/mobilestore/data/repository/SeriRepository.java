package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Product;
import com.seo.mobilestore.data.entity.Seri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeriRepository extends JpaRepository<Seri, Long> {
    boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Seri s WHERE s.product.id = :productId")
    void deleteByProduct(long productId);

    @Query(value = "SELECT i FROM Seri i WHERE i.product.id =:#{#product.id} AND i.name =:name")
    boolean findByNameAndProductId(String name, @Param("product") Product product);

    @Query(value = "SELECT s FROM Seri s WHERE s.product.id =:id and s.status= true")
    List<Seri> findByProductId(long id);
    Optional<Seri> findByName(String name);

    @Query("SELECT s FROM Seri s WHERE s.name = :seriName AND s.product.name = :productName AND s.status = true")
    Optional<Seri> findSeriByNameAndProductName(@Param("seriName") String seriName, @Param("productName") String productName);

    Optional<Seri> findById(long id);

}
