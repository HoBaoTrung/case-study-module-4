package com.codegym.mobilestore.repository;

import com.codegym.mobilestore.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface ICategoryRepository extends CrudRepository<Category, Integer> {

}
