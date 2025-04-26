package isd.be.htc.service.impl;

import isd.be.htc.config.security.CustomUserDetails;
import isd.be.htc.dto.CartItemDTO;
import isd.be.htc.dto.NotificationPayloadDTO;
import isd.be.htc.dto.OrderDTO;
import isd.be.htc.dto.OrderDetailsDTO;
import isd.be.htc.dto.OrderRequest;
import isd.be.htc.dto.PaymentDTO;
import isd.be.htc.model.*;
import isd.be.htc.model.enums.OrderStatus;
import isd.be.htc.repository.*;
import isd.be.htc.service.OrderService;
import isd.be.htc.service.SupabaseNotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SupabaseNotificationService notificationService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository,
            SupabaseNotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.sort(Comparator.comparing(Order::getOrderTime).reversed()); // mới nhất trước

        return orders.stream().map(order -> {
            List<OrderDetailsDTO> detailsDTO = order.getOrderDetails().stream().map(detail -> {
                return new OrderDetailsDTO(
                        detail.getProduct().getName(),
                        detail.getSize(),
                        detail.getSugarRate(),
                        detail.getIceRate(),
                        detail.getQuantity(),
                        detail.getUnitPrice());
            }).toList();

            Payment payment = order.getPayment();
            PaymentDTO paymentDTO = new PaymentDTO(
                    payment.getAmount(),
                    payment.getPaymentMethod(),
                    payment.getStatus(),
                    payment.getTransactionDate());

            return new OrderDTO(
                    order.getId(),
                    order.getUser().getId(),
                    order.getTotalAmount(),
                    order.getOrderTime(),
                    order.getStatus(),
                    paymentDTO,
                    detailsDTO,
                    order.getAddress(),
                    order.getPhoneNumber());
        }).toList();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    @Override
    public Order createOrder(OrderRequest orderRequest) {
        User user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            user = userDetails.getUser();
        }

        Order order = new Order();
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(orderRequest.getTotalPrice());
        order.setUser(user); // Có thể null
        order.setAddress(orderRequest.getAddress());
        order.setPhoneNumber(orderRequest.getPhoneNumber());

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(orderRequest.getTotalPrice());
        payment.setPaymentMethod(orderRequest.getPaymentMethod()); // -> Thêm field này vào OrderRequest
        payment.setStatus("UNPAID");
        payment.setTransactionDate(null); // Sẽ cập nhật sau khi thanh toán

        order.setPayment(payment);

        List<OrderDetail> details = new ArrayList<>();
        for (CartItemDTO item : orderRequest.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(item.getUnitPrice());

            // ✅ Lưu thông tin tuỳ chọn
            detail.setSize(item.getSize());
            detail.setSugarRate(item.getSugarRate());
            detail.setIceRate(item.getIceRate());

            details.add(detail);
        }

        order.setOrderDetails(details);
        Order savedOrder = orderRepository.save(order);

        // 👇 Gửi noti sau khi order được lưu thành công
        notificationService.sendNotification(
                new NotificationPayloadDTO(
                        "🛒 Đơn hàng mới",
                        String.format("Đơn hàng #%d - Tổng tiền %.0fK", savedOrder.getId(),
                                savedOrder.getTotalAmount()),
                        "new_order",
                        "/admin/orders", // 👈 Link frontend
                        String.valueOf(savedOrder.getId()) // 👈 Dùng nếu muốn xử lý thêm
                ));
        return savedOrder;
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
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        orders.sort(Comparator.comparing(Order::getOrderTime).reversed()); // mới nhất trước

        return orders.stream().map(order -> {
            List<OrderDetailsDTO> detailsDTO = order.getOrderDetails().stream().map(detail -> {
                return new OrderDetailsDTO(
                        detail.getProduct().getName(),
                        detail.getSize(),
                        detail.getSugarRate(),
                        detail.getIceRate(),
                        detail.getQuantity(),
                        detail.getUnitPrice());
            }).toList();

            Payment payment = order.getPayment();
            PaymentDTO paymentDTO = new PaymentDTO(
                    payment.getAmount(),
                    payment.getPaymentMethod(),
                    payment.getStatus(),
                    payment.getTransactionDate());

            return new OrderDTO(
                    order.getId(),
                    order.getUser().getId(),
                    order.getTotalAmount(),
                    order.getOrderTime(),
                    order.getStatus(),
                    paymentDTO,
                    detailsDTO,
                    order.getAddress(),
                    order.getPhoneNumber());
        }).toList();
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found!"));
        order.setStatus(status);
        orderRepository.save(order);
    }
}
