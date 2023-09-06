package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	Optional<User> findByOtp(String Otp);

	Optional<User> findById(long id);

	Page<User> findAllByFullNameContainingOrEmailContaining(String keyword, String keyword1, Pageable pageable);

	Page<User> findByStatusIsTrue(Pageable pageable);

}
