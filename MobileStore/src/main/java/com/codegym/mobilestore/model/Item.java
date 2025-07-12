package com.codegym.mobilestore.model;

import java.math.BigDecimal;

public class Item {
    private Product product;

    private int quantity;

    public BigDecimal getLineTotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Item(Product product, int quantity) {
        super();
        this.product = product;
        this.quantity = quantity;
    }

    public Item() {
        super();
    }
}
