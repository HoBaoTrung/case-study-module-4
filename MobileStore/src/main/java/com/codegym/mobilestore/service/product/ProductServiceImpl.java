package com.codegym.mobilestore.service.product;

import com.codegym.mobilestore.model.Item;
import com.codegym.mobilestore.model.Product;
import com.codegym.mobilestore.repository.IProductRepository;
import com.codegym.mobilestore.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
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

    @Autowired
    private EntityManager entityManager;

    public boolean checkProductQuantity(int productId, int quantity) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_check_product_quantity");

        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);  // productId
        query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);  // quantity
        query.registerStoredProcedureParameter(3, Boolean.class, ParameterMode.OUT); // enough

        query.setParameter(1, productId);
        query.setParameter(2, quantity);

        query.execute();

        return (Boolean) query.getOutputParameterValue(3); // lấy OUT parameter theo vị trí
    }


    @Override
    public void checkAllProductQuantities(List<Item> cart) throws Exception {
        for (Item item : cart) {
            boolean enough = checkProductQuantity(
                    item.getProduct().getProductId(),
                    item.getQuantity()
            );

            if (!enough) {
                Product p=getProductById(item.getProduct().getProductId());
                throw new SQLException( p.getProductName() + " chỉ còn " +
                        p.getStockQuantity() + " sản phẩm.");
            }
        }
    }

//    @Override
//    @Transactional
//    public void updateProductQuantities(List<Item> cart) throws SQLException {
//        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_update_product_quantity");
//        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
//        query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
//
//            for (Item item : cart) {
//                Product p = productRepository.findById(item.getProduct().getProductId())
//                        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm " + item.getProduct().getProductName()));
//                query.setParameter(1, item.getProduct().getProductId());
//                query.setParameter(2, item.getQuantity());
//                query.executeUpdate();
//            }
//    }

    @Autowired
    private DataSource dataSource;

    @Override
    public void updateProductQuantities(List<Item> cart) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "{CALL sp_update_product_quantity(?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                for (Item item : cart) {
                    stmt.setInt(1, item.getProduct().getProductId());
                    stmt.setInt(2, item.getQuantity());
                    stmt.execute();
                }
            }
        }
    }

}
