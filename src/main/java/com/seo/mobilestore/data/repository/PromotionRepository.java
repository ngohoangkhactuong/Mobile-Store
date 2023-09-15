package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    boolean existsByDiscountCode(String discountCode);

    boolean existsByDiscountCodeAndIdNot(String discountCode, long id);

    Optional<Promotion> findById(long id);

    Page<Promotion> findAllByDiscountCodeContainingAndStatusIsTrue(String discountCode, Pageable pageable);

    Page<Promotion> findByStatusIsTrue(Pageable pageable);

}
