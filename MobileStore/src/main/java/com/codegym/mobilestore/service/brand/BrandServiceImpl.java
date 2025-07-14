package com.codegym.mobilestore.service.brand;

import com.codegym.mobilestore.model.Brand;
import com.codegym.mobilestore.model.Category;
import com.codegym.mobilestore.repository.IBrandRepository;
import com.codegym.mobilestore.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService {
@Autowired
private IBrandRepository brandRepository;
    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand delete(Brand brand) {
        try{
            brandRepository.delete(brand);
            return brand;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Iterable<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrandById(Integer id) {
        return brandRepository.findById(id).get();
    }
}
