package com.seo.mobilestore.data.repository;

import com.seo.mobilestore.data.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    @Query(value = "SELECT p FROM Product p WHERE p.status = true ORDER BY p.createdDate DESC")
    Page<Product> findAllActive(Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.status = true AND p.category.id= :id ORDER BY p.createdDate DESC")
    Page<Product> findAllActive(long id, Pageable pageable);

    List<Product> findByName(String name);

    @Query("SELECT p FROM Product p " +
            "WHERE (p.name LIKE %:keyword% " +
            "OR p.manufacturer.name LIKE %:keyword% " +
            "OR p.category.name LIKE %:keyword%) and p.status = true")
    Page<Product> findProductByKeywordCustomer(String keyword, Pageable pageable);


    @Query("SELECT p FROM Product p " +
            "WHERE (p.name LIKE %:keyword% " +
            "OR p.manufacturer.name LIKE %:keyword% " +
            "OR p.category.name LIKE %:keyword%)")
    Page<Product> findProductByKeywordAdmin(String keyword, Pageable pageable);

    Optional<Product> findById(long id);

    @Query("SELECT p FROM Product p WHERE p.id = :id")
    List<Product> findProductsById(long id);

    @Query(value = "SELECT p FROM Product p " +
            "WHERE p.status = true " +
            "AND (p.manufacturer.id = :manufacturerId OR (:manufacturerId =0 AND p.manufacturer.id != 0))" +
            "AND (p.category.id = :categoryId OR (:categoryId = 0 AND p.category.id != 0))" +
            "AND p.name LIKE %:keyword% " +
            "AND p.price >= :lowerPrice AND p.price < :higherPrice " +
            "ORDER BY p.price ASC" + ", p.createdDate DESC")
    Page<Product> findProductsByFullFilterCustomer(@Param("manufacturerId") long manufacturerId,
                                                   @Param("categoryId") long categoryId,
                                                   @Param("keyword") String keyword,
                                                   @Param("lowerPrice") BigDecimal lowerPrice,
                                                   @Param("higherPrice") BigDecimal higherPrice,
                                                   Pageable pageable);

    @Query(value = "SELECT p FROM Product p " +
            "WHERE (p.manufacturer.id = :manufacturerId OR (:manufacturerId =0 AND p.manufacturer.id != 0))" +
            "AND (p.category.id = :categoryId OR (:categoryId = 0 AND p.category.id != 0))" +
            "AND p.name LIKE %:keyword% " +
            "AND p.price >= :lowerPrice AND p.price < :higherPrice " +
            "ORDER BY p.price ASC" + ", p.createdDate DESC")
    Page<Product> findProductsByFullFilterAdmin(@Param("manufacturerId") long manufacturerId,
                                                @Param("categoryId") long categoryId,
                                                @Param("keyword") String keyword,
                                                @Param("lowerPrice") BigDecimal lowerPrice,
                                                @Param("higherPrice") BigDecimal higherPrice,
                                                Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.id DESC")
    List<Product> findNewProductWithLimit();

    @Query("SELECT p FROM Product p " +
            "WHERE p.category.id = :categoryId")
    Page<Product> showListProductPaginationAdmin(long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE p.category.id = :categoryId AND p.status = true")
    Page<Product> showListProductPaginationCustomer(long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE p.category.id = :categoryId AND p.manufacturer.id = :manufacturerId")
    Page<Product> showListProductByCategoryAndManufacturerAdmin(long categoryId, long manufacturerId, Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE p.category.id = :categoryId AND p.manufacturer.id = :manufacturerId AND p.status = true")
    Page<Product> showListProductByCategoryAndManufacturerCustomer(long categoryId, long manufacturerId, Pageable pageable);

    @Query("select p FROM Product p " +
            "WHERE p.category.id = :categoryId AND p.status = true")
    Page<Product> findActiveProductByCategory(long categoryId, Pageable pageable);

    @Query("select p FROM Product p " +
            "WHERE p.category.id = :manufactureId AND p.status = true")
    Page<Product> findActiveProductByManufacturer(long manufactureId, Pageable pageable);


    @Query(value = "SELECT p FROM Product p " +
            "WHERE p.status = true " +
            "AND p.price >= :lowerPrice AND p.price < :higherPrice " +
            "ORDER BY p.price ASC" + ", p.createdDate DESC")
    Page<Product> FilterProductsByPriceCustomer(@Param("lowerPrice") BigDecimal lowerPrice,
                                                @Param("higherPrice") BigDecimal higherPrice,
                                                Pageable pageable);

    @Query(value = "SELECT p FROM Product p " +
            "WHERE p.price >= :lowerPrice AND p.price < :higherPrice " +
            "ORDER BY p.price ASC" + ", p.createdDate DESC")
    Page<Product> FilterProductsByPriceAdmin(@Param("lowerPrice") BigDecimal lowerPrice,
                                             @Param("higherPrice") BigDecimal higherPrice,
                                             Pageable pageable);

}

