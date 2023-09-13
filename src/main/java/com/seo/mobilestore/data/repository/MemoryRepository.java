package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoryRepository extends JpaRepository<Memory  , Long> {
}
