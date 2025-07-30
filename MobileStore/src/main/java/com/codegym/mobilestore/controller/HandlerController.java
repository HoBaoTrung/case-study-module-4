package com.codegym.mobilestore.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HandlerController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = "Đã xảy ra lỗi không xác định.";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                message = "Trang bạn yêu cầu không tồn tại (404).";
                model.addAttribute("message", message);
                return "err/error_404";
            }
//            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                message = "Lỗi máy chủ nội bộ (500).";
//                model.addAttribute("message", message);
//                return "err/error_500";
//            }
        }

        model.addAttribute("message", message);
        return "err/error";
    }
}