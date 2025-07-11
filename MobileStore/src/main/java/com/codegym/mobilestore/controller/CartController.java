package com.codegym.mobilestore.controller;

import com.codegym.mobilestore.model.Item;
import com.codegym.mobilestore.model.Product;
import com.codegym.mobilestore.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private ProductService productService;


    @PostMapping
    @ResponseBody
    public ResponseEntity<String> handleCartAction(
            @RequestParam("id") Integer productId,
            HttpSession session) {

        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sản phẩm không tồn tại");
        }
        // Lấy giỏ hàng từ session hoặc tạo mới
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        // Thêm sản phẩm vào giỏ (tăng số lượng nếu đã tồn tại)
        int index = getIndex(productId, cart);
        if (index == -1) {
            cart.add(new Item(product, 1));
        } else {
            Item item = cart.get(index);
            item.setQuantity(item.getQuantity() + 1);
        }

        return ResponseEntity.ok("Đã thêm sản phẩm vào giỏ hàng");
    }

    private int getIndex(int id, List<Item> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProduct().getProductId() == id) {
                return i;
            }
        }
        return -1;
    }

}
