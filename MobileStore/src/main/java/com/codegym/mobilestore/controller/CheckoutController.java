package com.codegym.mobilestore.controller;

import com.codegym.mobilestore.model.Item;
import com.codegym.mobilestore.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String viewFormCheckout(HttpSession session, RedirectAttributes redirectAttributes) {
        List<Item> cart = (List<Item>) session.getAttribute("cart");

        try {
            productService.checkAllProductQuantities(cart);
            return "checkout/form";
        } catch (Exception e) {
            // Đưa lỗi sang /carts thông qua flash attribute
            redirectAttributes.addFlashAttribute("checkoutError", e.getMessage());
            return "redirect:/carts";
        }
    }

//    @PostMapping
//    public String processFormCheckout(Model model, HttpSession session, RedirectAttributes redirectAttributes,
//                                      @RequestParam Map<String,String> params) {
//        List<Item> cart = (List<Item>) session.getAttribute("cart");
//        if (cart == null || cart.isEmpty()) {
//            redirectAttributes.addFlashAttribute("checkoutError", "No cart found");
//            return "redirect:/carts";
//        }
//
////        String customerName = params.get("customerName");
////        String phone = params.get("phone");
////        String address = params.get("address");
////        String paymentMethod = params.get("paymentMethod");
//
//
//    }
}
