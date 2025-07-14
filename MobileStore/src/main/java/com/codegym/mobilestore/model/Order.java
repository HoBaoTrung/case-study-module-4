package com.codegym.mobilestore.model;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate = LocalDateTime.now();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;


    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private ShipInfo shipInfo;

    private String paymentMethod;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Order() {}

    public Order(Integer id, BigDecimal totalPrice, LocalDateTime orderDate, String paymentMethod, User user) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }


    public User getCustomer() {
        return user;
    }

    public void setCustomer(User user) {
        this.user = user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public ShipInfo getShipInfo() {
        return shipInfo;
    }

    public void setShipInfo(ShipInfo shipInfo) {
        this.shipInfo = shipInfo;
    }
}
