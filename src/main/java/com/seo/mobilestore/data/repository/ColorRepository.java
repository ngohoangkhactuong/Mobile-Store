package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color , Long> {
}
