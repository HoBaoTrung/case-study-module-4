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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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
            String minPriceStr = rangeValues[0];
            String maxPriceStr = rangeValues[1];
            model.addAttribute("minPrice", minPriceStr);
            model.addAttribute("maxPrice", maxPriceStr);
            BigDecimal minPrice = new BigDecimal(minPriceStr);
            BigDecimal maxPrice = new BigDecimal(maxPriceStr);
            BigDecimal range = maxPrice.subtract(minPrice);

            int step = range.compareTo(BigDecimal.valueOf(5_000_000)) > 0 ? 100_000_000
                    : range.compareTo(BigDecimal.valueOf(1_000_000)) > 0 ? 50_000_000
                    : 1000;

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
    public String showAddForm(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("product", new Product());
        model.addAttribute("formAction","/products/add");

        return "product/product-form";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product
            ,@RequestParam("imageFile") MultipartFile imageFile
    ) {
        productService.save(product, imageFile);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("product", product);
        model.addAttribute("formAction","/products/" + product.getProductId() + "/edit");
        return "product/product-form";
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@ModelAttribute("product") Product product,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile ) {
        productService.save(product, imageFile);
        return "redirect:/products/" + product.getProductId();
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<String> deleteProduct(@RequestParam("id") Integer id) {
        Product p = productService.deleteProduct(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy sản phẩm");
        }
        return ResponseEntity.ok("Đã xóa sản phầm ra khỏi giỏ hàng");
    }


}
