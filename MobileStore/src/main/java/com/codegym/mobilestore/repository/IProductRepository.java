package com.codegym.mobilestore.repository;

import com.codegym.mobilestore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface IProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    @Query("SELECT MIN(price)  FROM Product")
    BigDecimal getMinPrice();

    @Query("SELECT MAX(price)  FROM Product")
    BigDecimal getMaxPrice();


}
