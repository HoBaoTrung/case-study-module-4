package com.codegym.mobilestore.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brandId;
    private String brandName;
    private String country;
    private String website;
    private String imageUrl;

    @OneToMany(mappedBy = "brand")
    private List<Product> products;

    public Brand(String imageUrl, String website, String country, String brandName, Integer brandId) {
        this.imageUrl = imageUrl;
        this.website = website;
        this.country = country;
        this.brandName = brandName;
        this.brandId = brandId;
    }
    public Brand() {}

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
