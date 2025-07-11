package com.codegym.mobilestore.service.category;

import com.codegym.mobilestore.model.Category;
import com.codegym.mobilestore.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category delete(Category category) {
        try {
            categoryRepository.delete(category);
            return category;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }
}
