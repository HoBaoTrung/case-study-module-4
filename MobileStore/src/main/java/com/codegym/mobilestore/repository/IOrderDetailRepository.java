package com.codegym.mobilestore.repository;

import com.codegym.mobilestore.model.OrderDetail;
import com.codegym.mobilestore.model.OrderDetailId;
import org.springframework.data.repository.CrudRepository;

public interface IOrderDetailRepository extends CrudRepository<OrderDetail, OrderDetailId> {
}
