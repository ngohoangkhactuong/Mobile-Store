package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Technical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicalRepository extends JpaRepository<Technical , Long> {
    @Query("select t from Technical t where t.name = :name")
    Boolean existsByName(String name);
}
