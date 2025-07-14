package com.codegym.mobilestore.repository;

import com.codegym.mobilestore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Order, Integer> {
}
