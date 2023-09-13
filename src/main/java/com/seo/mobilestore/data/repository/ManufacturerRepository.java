package com.seo.mobilestore.data.repository;

import java.util.Optional;
import com.seo.mobilestore.data.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    boolean existsByName(String name);
    Optional<Manufacturer> findById(long id);
}
