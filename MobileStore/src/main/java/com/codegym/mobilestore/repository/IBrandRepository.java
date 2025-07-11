package com.codegym.mobilestore.repository;

import com.codegym.mobilestore.model.Brand;
import org.springframework.data.repository.CrudRepository;

public interface IBrandRepository extends CrudRepository<Brand, Integer> {
}
