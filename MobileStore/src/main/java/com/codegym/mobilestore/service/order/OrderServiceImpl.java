package com.codegym.mobilestore.service.order;

import com.codegym.mobilestore.model.*;
import com.codegym.mobilestore.repository.IOrderDetailRepository;
import com.codegym.mobilestore.repository.IOrderRepository;
import com.codegym.mobilestore.repository.IShipInfoRepository;
import com.codegym.mobilestore.service.product.ProductService;
import com.codegym.mobilestore.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private IOrderRepository orderRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private IShipInfoRepository iShipInfoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private IOrderDetailRepository orderDetailRepo;

    @Override
    public int insertOrder(Long customerID, double totalPrice, String paymentMethod) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_insert_orders");

        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, Double.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.OUT);

        query.setParameter(1, customerID);
        query.setParameter(2, totalPrice);
        query.setParameter(3, paymentMethod);
        query.execute();

        return (Integer) query.getOutputParameterValue(4);
    }

    @Override
    @Transactional
    public Order checkOut(List<Item> cart, Map<String, String>  customerData) throws Exception {
        // 1. Kiểm tra tồn kho
        productService.checkAllProductQuantities(cart);

        // 2. Trừ kho
        productService.updateProductQuantities(cart);

        // 3. Luu thong tin ship
        ShipInfo customer = new ShipInfo();
        customer.setCustomerName(customerData.get("customerName"));
        customer.setPhoneNumber(customerData.get("phone"));
        customer.setAddress(customerData.get("address"));
        customer.setPaymentMethod(customerData.get("paymentMethod"));


        // 4. Tạo đơn hàng
        Order order = new Order();
        order.setCustomer(userService.getCurrentUser());
        order.setPaymentMethod(customer.getPaymentMethod());
        order.setTotalPrice(calculateTotal(cart));
        order.setShipInfo(customer);
        orderRepo.save(order);

        customer.setUser(userService.getCurrentUser());
        customer.setOrder(order);
        iShipInfoRepository.save(customer);

        List<OrderDetail> details = cart.stream().map(item -> {
            OrderDetail od = new OrderDetail();

            OrderDetailId id = new OrderDetailId(); // ✅ Tạo mới cho mỗi phần tử
            id.setOrderId(order.getId());
            id.setProductId(item.getProduct().getProductId());

            od.setId(id);
            od.setOrder(order);
            od.setProduct(item.getProduct());
            od.setQuantity(item.getQuantity());

            return od;
        }).collect(Collectors.toList());
        order.setOrderDetails(details);

        orderRepo.save(order);
//        orderDetailRepo.saveAll(details); // ✅ Dùng saveAll để lưu danh sách


        return order;

    }

    private BigDecimal calculateTotal(List<Item> cart) {
        return cart.stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }



}
