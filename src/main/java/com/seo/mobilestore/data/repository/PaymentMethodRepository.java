package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    boolean existsByName(String name);

    Optional<PaymentMethod> findByName(String name);

    @Query("Select p from PaymentMethod p Where p.status = true and p.id = :id")
    Optional<PaymentMethod> findByIdPagement(long id);

    List<PaymentMethod> findByStatusIsTrue();

    @Query("Select p from PaymentMethod p Where p.status = true")
    Page<PaymentMethod> findAllPagment(Pageable pageable);
}
