package com.codegym.mobilestore.service.brand;

import com.codegym.mobilestore.model.Brand;
import com.codegym.mobilestore.service.IGeneralService;

public interface BrandService extends IGeneralService<Brand> {
    Brand getBrandById(Integer id);

}
