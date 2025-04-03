package isd.be.htc.service.impl;

import isd.be.htc.config.security.CustomUserDetails;
import isd.be.htc.dto.OrderRequest;
import isd.be.htc.model.*;
import isd.be.htc.repository.*;
import isd.be.htc.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    @Override
    public Order createOrder(OrderRequest orderRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated!");
        }
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();
        Long userId = user.getId();

        Order order = new Order();
        order.setTotalAmount(orderRequest.getTotalPrice());
        order.setUser(user);
        order.setOrderTime(LocalDateTime.now());

        List<OrderDetail> orderDetails = orderRequest.getItems().stream()
                .map(cartItem -> {
                    OrderDetail od = new OrderDetail();
                    od.setOrder(order);
                    od.setProduct(productRepository.findById(cartItem.getProductId()).orElseThrow());
                    od.setQuantity(cartItem.getQuantity());
                    od.setUnitPrice(cartItem.getUnitPrice());
                    return od;
                }).toList();
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);
        return order;
    }


    @Override
    public Order updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id).map(order -> {
            order.setTotalAmount(orderDetails.getTotalAmount());
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found!"));
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
