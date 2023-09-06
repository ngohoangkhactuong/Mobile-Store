package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

    boolean existsByName(String name);

    Optional<Status> findByName(String name);

}
