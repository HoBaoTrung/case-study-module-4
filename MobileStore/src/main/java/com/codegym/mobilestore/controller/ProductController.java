package com.codegym.mobilestore.controller;

import com.codegym.mobilestore.model.Brand;
import com.codegym.mobilestore.model.Category;
import com.codegym.mobilestore.model.Product;
import com.codegym.mobilestore.repository.IProductRepository;
import com.codegym.mobilestore.service.brand.BrandService;
import com.codegym.mobilestore.service.category.CategoryService;
import com.codegym.mobilestore.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private IProductRepository productRepository;

    @GetMapping
    public String getProducts(Model model,
                              @RequestParam MultiValueMap<String, String> params,
                              @PageableDefault(size = 3) Pageable pageable,
                              @RequestParam(value = "fragment", required = false, defaultValue = "false") boolean fragment) {
        Page<Product> page;

        // Nếu có filter (AJAX hoặc form submit)
        if (params.isEmpty()) {
            page = productService.findAll(pageable);
        } else {
            page = productService.searchProducts(params, pageable);
        }
        model.addAttribute("products", page.getContent());
        int currentPage = pageable.getPageNumber();
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("totalPages", page.getTotalPages());

        // Luôn thêm các biến nếu là trang đầy đủ
        if (!fragment) {
            String[] rangeValues = productService.getRangePrice();
            model.addAttribute("minPrice", rangeValues[0]);
            model.addAttribute("maxPrice", rangeValues[1]);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("brands", brandService.findAll());
        }

        return fragment
                ? "fragments/productList :: productListContent"
                : "product/list";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable("id") Integer id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/error";
        }
        model.addAttribute("product", product);
        return "product/view"; // trỏ đến file view.html
    }

    @GetMapping("/add")
    @Secured("ROLE_ADMIN")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/product-form";
    }

    @PostMapping("/add")
    @Secured("ROLE_ADMIN")
    public String addProduct(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    @Secured("ROLE_ADMIN")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product/product-form";
    }

    @PostMapping("/{id}/edit")
    @Secured("ROLE_ADMIN")
    public String updateProduct(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/products/" + product.getProductId();
    }


}
