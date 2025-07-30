package com.codegym.mobilestore.controller;

import com.codegym.mobilestore.dto.Create;
import com.codegym.mobilestore.dto.UserDTO;
import com.codegym.mobilestore.model.User;
import com.codegym.mobilestore.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@RestController
//@RequestMapping("/api/users")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String create(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserDTO());
        }
        return "user/index";
    }

    // Tạo mới người dùng
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") @Valid UserDTO userDTO,
                                  BindingResult result,
                                  Model model) {

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }

        if (result.hasErrors()) {
            model.addAttribute("showRegister", true);
            return "user/index";
        }

        userService.create(userDTO);
        return "redirect:/login?registerSuccess";
    }

    // Cập nhật người dùng
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody UserDTO userDTO,
                                    BindingResult result) {
        userDTO.setId(id); // gán ID vào DTO để validator hoạt động đúng
        System.out.println(result);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        User updated = userService.update(id, userDTO);
        return ResponseEntity.ok(updated);
    }

    // Xem người dùng
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}
