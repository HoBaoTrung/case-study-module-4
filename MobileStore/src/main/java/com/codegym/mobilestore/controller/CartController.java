package com.codegym.mobilestore.controller;

import com.codegym.mobilestore.dto.CartUpdateRequest;
import com.codegym.mobilestore.model.Item;
import com.codegym.mobilestore.model.Product;
import com.codegym.mobilestore.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String viewCart(HttpSession session, Model model) {
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (cart == null) cart = new ArrayList<>();
        else{
            for (Item item : cart) {
                totalPrice =  totalPrice.add(item.getLineTotal());
            }
        }
        System.out.println("Checkout Error: " + model.getAttribute("checkoutError"));
        model.addAttribute("cartItems", cart);
        model.addAttribute("totalPrice", totalPrice);
        return "cart/view"; // trang hiển thị giỏ hàng
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> addCart(
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

    @PutMapping
    @ResponseBody
    public ResponseEntity<String> updateCart(@RequestBody CartUpdateRequest request,
                                             HttpSession session) {
        String type = request.getType();
        Integer productId = request.getId();

        List<Item> cart = (List<Item>) session.getAttribute("cart");

        if (cart != null) {
            int index = getIndex(productId, cart);
            if (index != -1) {
                Item item = cart.get(index);
                int quantity = item.getQuantity();

                if ("add".equals(type)) {
                    item.setQuantity(quantity + 1);
                } else if ("sub".equals(type)) {
                    if (quantity > 1) {
                        item.setQuantity(quantity - 1);
                    } else {
                        cart.remove(index);
                    }
                }
            }
        }

        return ResponseEntity.ok("Đã cập nhật giỏ hàng");
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<String> deleteCart(@RequestParam("id") Integer productId, HttpSession session) {
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        Iterator<Item> iterator = cart.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getProduct().getProductId() == productId) {
                iterator.remove();
                break;
            }
        }
        return ResponseEntity.ok("Đã xóa sản phầm ra khỏi giỏ hàng");
    }

    @GetMapping("/fragment")
    public String getCartFragment(Model model, HttpSession session) {
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        BigDecimal totalPrice = BigDecimal.ZERO;

            for (Item item : cart) {
                totalPrice =  totalPrice.add(item.getLineTotal());
            }

        model.addAttribute("cartItems", cart);
        model.addAttribute("totalPrice", totalPrice);
        return "fragments/cartList :: cartContent";
    }


}
