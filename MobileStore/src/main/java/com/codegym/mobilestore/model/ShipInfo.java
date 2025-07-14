package com.codegym.mobilestore.model;

import javax.persistence.*;
@Entity
public class ShipInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String phoneNumber;
    private String address;
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public ShipInfo() {}

    public ShipInfo(Long id, String customerName, String phoneNumber, String address, String paymentMethod) {
        this.id = id;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.paymentMethod = paymentMethod;
    }

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}
