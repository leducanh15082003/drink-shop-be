package isd.be.htc.controller;

import isd.be.htc.dto.OrderDTO;
import isd.be.htc.dto.OrderDetailsDTO;
import isd.be.htc.dto.OrderRequest;
import isd.be.htc.dto.PaymentDTO;
import isd.be.htc.model.Order;
import isd.be.htc.model.Payment;
import isd.be.htc.model.enums.OrderStatus;
import isd.be.htc.service.OrderService;
import isd.be.htc.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private static final String GUEST_ORDER_ID = "GUEST_ORDER_ID";

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        Optional<Order> order = orderService.getOrderById(id);
//        return order.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        try {
            Order updatedOrder = orderService.updateOrder(id, orderDetails);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        try {
            orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public List<OrderDTO> getOrdersByUserId(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.findUserByEmail(userDetails.getUsername()).getId();
        return orderService.getOrdersByUserId(userId);
    }

    @PostMapping("/guest/{orderId}")
    public ResponseEntity<Void> recordGuestOrder(
            @PathVariable Long orderId,
            HttpSession session) {
        session.setAttribute(GUEST_ORDER_ID, orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/guest")
    public ResponseEntity<List<OrderDTO>> getGuestOrder(HttpSession session) {
        Object obj = session.getAttribute(GUEST_ORDER_ID);
        if (!(obj instanceof Long)) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        Long orderId = (Long) obj;
        OrderDTO dto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(Collections.singletonList(dto));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        List<OrderDetailsDTO> orderDetails = order.getOrderDetails().stream().map(detail -> {
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

        OrderDTO orderDTO = new OrderDTO(
                order.getId(),
                order.getUser() == null ? null : order.getUser().getId(),
                order.getUser() == null ? null : order.getUser().getFullName(),
                order.getTotalAmount(),
                order.getOrderTime(),
                order.getStatus(),
                paymentDTO,
                orderDetails,
                order.getAddress(),
                order.getPhoneNumber(),
                order.getDiscountAmount(),
                order.getNote());

        return ResponseEntity.ok(orderDTO);
    }
}
