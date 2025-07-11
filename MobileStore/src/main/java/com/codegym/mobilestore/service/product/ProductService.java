package com.codegym.mobilestore.service.product;

import com.codegym.mobilestore.model.Product;
import com.codegym.mobilestore.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface ProductService extends IGeneralService<Product> {
    Page<Product> searchProducts(MultiValueMap<String,String> params, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
    String[] getRangePrice();
    Product getProductById(Integer id);
}
