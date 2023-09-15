package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    boolean existsByName(String name);

    Optional<Object> findByName(String name);

    List<Categories> findByStatusIsTrue();

    Optional<Categories> findById(long id);

}
