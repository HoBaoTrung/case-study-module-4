package com.codegym.mobilestore.service.product;

import com.codegym.mobilestore.model.Product;
import com.codegym.mobilestore.repository.IProductRepository;
import com.codegym.mobilestore.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    IProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product delete(Product product) {
        try {
            productRepository.delete(product);
            return product;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }


    @Override
    public Page<Product> searchProducts(MultiValueMap<String, String> params, Pageable pageable) {
        // Xử lý keyword - an toàn với null/empty
        String keyword = params.getFirst("keyword");
        keyword = (keyword != null) ? keyword.trim() : "";

        // Xử lý minPrice và maxPrice
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        try {
            String minPriceStr = params.getFirst("minPrice");
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                minPrice = new BigDecimal(minPriceStr.trim());
            }

            String maxPriceStr = params.getFirst("maxPrice");
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                maxPrice = new BigDecimal(maxPriceStr.trim());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá trị minPrice hoặc maxPrice không hợp lệ", e);
        }


        List<String> stringList = params.get("cateID");
        List<Integer> categories =(stringList != null)?
                 stringList.stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList()): null;

        // Xử lý brands - lấy tất cả giá trị nếu có nhiều
        stringList = params.get("brandID");
        List<Integer> brads =(stringList != null)?
                stringList.stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList()): null;

        Specification<Product> spec = ProductSpecification.filterProducts(
                keyword, minPrice, maxPrice, brads, categories
        );

        return productRepository.findAll(spec, pageable);

    }

    @Override
    public String[] getRangePrice() {
        String minPrice = productRepository.getMinPrice().toString();
        String maxPrice = productRepository.getMaxPrice().toString();

        return new String[]{minPrice, maxPrice};
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }
}
