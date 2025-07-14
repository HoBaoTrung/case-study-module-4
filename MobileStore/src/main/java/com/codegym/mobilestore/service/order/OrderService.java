package com.codegym.mobilestore.service.order;

import com.codegym.mobilestore.model.Item;
import com.codegym.mobilestore.model.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
   int insertOrder( Long customerID, double totalPrice, String paymentMethod);

    Order checkOut(List<Item> cart, Map<String, String>  customer) throws Exception;
}
