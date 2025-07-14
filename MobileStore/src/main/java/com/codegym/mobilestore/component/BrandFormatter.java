package com.codegym.mobilestore.component;

import com.codegym.mobilestore.model.Brand;
import com.codegym.mobilestore.service.brand.BrandService;
import com.codegym.mobilestore.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;
@Component
public class BrandFormatter implements Formatter<Brand> {
    private final BrandService brandService;

    @Autowired
    public BrandFormatter(BrandService brandService) {
        this.brandService = brandService;
    }

    @Override
    public Brand parse(String text, Locale locale) {
        try {
            Integer id = Integer.parseInt(text);
            return brandService.getBrandById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Không tìm thấy Brand với id: " + text);
        }
    }

    @Override
    public String print(Brand object, Locale locale) {
        return (object != null) ? String.valueOf(object.getBrandId()) : "";
    }
}
