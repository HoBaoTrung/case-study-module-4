package com.codegym.mobilestore.component;

import com.codegym.mobilestore.model.Category;
import com.codegym.mobilestore.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

public class CategoryFormatter implements Formatter<Category> {

    private final CategoryService categoryService;

    public CategoryFormatter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public Category parse(String text, Locale locale) {
        try {
            Integer id = Integer.parseInt(text);
            return categoryService.getCategoryById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Không tìm thấy Category với id: " + text);
        }
    }

    @Override
    public String print(Category object, Locale locale) {
        return (object != null) ? String.valueOf(object.getCategoryId()) : "";
    }
}
