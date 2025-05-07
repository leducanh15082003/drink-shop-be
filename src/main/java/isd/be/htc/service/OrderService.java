package isd.be.htc.service;

import isd.be.htc.dto.MonthlyRevenueDTO;
import isd.be.htc.dto.OrderDTO;
import isd.be.htc.dto.OrderRequest;
import isd.be.htc.dto.StatisticDTO;
import isd.be.htc.model.Order;
import isd.be.htc.model.enums.OrderStatus;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(Long id);

    @Transactional
    Order createOrder(OrderRequest orderRequest);

    Order updateOrder(Long id, Order orderDetails);

    void updateOrderStatus(Long id, OrderStatus status);

    void deleteOrder(Long id);

    List<OrderDTO> getOrdersByUserId(Long userId);

    StatisticDTO getOrderStats();

    StatisticDTO getMonthlyRevenue();

    List<MonthlyRevenueDTO> getMonthlyRevenueData(int year);
}
