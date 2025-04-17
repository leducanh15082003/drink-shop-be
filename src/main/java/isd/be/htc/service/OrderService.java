package isd.be.htc.service;

import isd.be.htc.dto.OrderDTO;
import isd.be.htc.dto.OrderRequest;
import isd.be.htc.model.Order;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();

    Optional<Order> getOrderById(Long id);

    @Transactional
    Order createOrder(OrderRequest orderRequest);

    Order updateOrder(Long id, Order orderDetails);

    void deleteOrder(Long id);

    List<OrderDTO> getOrdersByUserId(Long userId);
}
