package isd.be.htc.service;

import isd.be.htc.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    Order createOrder();
    Order updateOrder(Long id, Order orderDetails);
    void deleteOrder(Long id);
    List<Order> getOrdersByUserId(Long userId);
}
