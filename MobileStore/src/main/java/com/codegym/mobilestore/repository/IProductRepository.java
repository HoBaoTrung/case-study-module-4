package com.codegym.mobilestore.repository;

import com.codegym.mobilestore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface IProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

//    @Query("SELECT p FROM Product p " +
//            "WHERE (:productName IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
//            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
//            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
//            "AND (:brandIds IS EMPTY OR p.brand.brandId IN :brandIds) " +
//            "AND (:categoryIds IS EMPTY OR p.category.categoryId IN :categoryIds)")
//    Page<Product> searchProducts(@Param("productName") String productName,
//                                 @Param("minPrice") BigDecimal minPrice,
//                                 @Param("maxPrice") BigDecimal maxPrice,
//                                 @Param("brandIds") List<Integer> brandIds,
//                                 @Param("categoryIds") List<Integer> categoryIds,
//                                 Pageable pageable);



    @Query("SELECT MIN(price)  FROM Product")
    BigDecimal getMinPrice();

    @Query("SELECT MAX(price)  FROM Product")
    BigDecimal getMaxPrice();


}
