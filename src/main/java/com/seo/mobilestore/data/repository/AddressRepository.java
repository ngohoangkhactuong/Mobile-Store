package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findAddressById(long id);

    @Query("SELECT a FROM Address a WHERE a.defaults = true and a.user.id = :userId")
    Address findDefaultAddress(long userId);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId")
    List<Address> findAddressByUserIdCustomer(long userId);

    @Query("SELECT a FROM Address a")
    List<Address> findAddressByUserIdAdmin();

    @Query("SELECT a FROM Address a WHERE a.location = :location AND a.user.id =:idUser")

    List<Address> existsByLocation(String location, long idUser);

}
