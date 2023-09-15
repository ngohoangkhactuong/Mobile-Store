package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<Color , Long> {
    @Query(value = "SELECT c FROM Color c WHERE c.product.id =:id and c.status= true")
    List<Color> findByProductId(long id);

    @Query("SELECT c FROM Color c WHERE c.name = :colorName AND c.product.id = :productId")
    Optional<Color> findColorByNameAndProductId(@Param("colorName") String colorName, @Param("productId") long productId);

    boolean existsByName (String name);

    @Query("SELECT c FROM Color c WHERE c.name = :colorName AND c.product.id = :productId")
    List<Color> findColorByNameAndProId(@Param("colorName") String colorName, @Param("productId") Long productId);

    Optional<Color> findById(int id);

}
