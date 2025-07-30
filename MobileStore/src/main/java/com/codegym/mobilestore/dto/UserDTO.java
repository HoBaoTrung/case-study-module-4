package com.codegym.mobilestore.dto;


import com.codegym.mobilestore.annotation.Unique;
import com.codegym.mobilestore.model.Role;
import com.codegym.mobilestore.model.User;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;
public class UserDTO {
    private Long id;

    @NotBlank
    @Unique(entityClass = User.class, fieldName = "username", message = "Username đã được sử dụng")
    private String username; // đổi từ name -> username để thống nhất với login và hiển thị

    @NotBlank(groups = Create.class)
    private String password;

    @Transient // không lưu vào DB
    @NotBlank(groups = Create.class)
    private String confirmPassword;

    @NotBlank
    @Email
    @Unique(entityClass = User.class, fieldName = "email", message = "Email đã được sử dụng")
    private String email;

    private Set<Role> roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, Set<Role> roles, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
