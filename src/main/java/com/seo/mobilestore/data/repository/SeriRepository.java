package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Seri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriRepository extends JpaRepository<Seri, Long> {
}
