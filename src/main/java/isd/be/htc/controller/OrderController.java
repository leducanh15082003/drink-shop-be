package isd.be.htc.controller;

import isd.be.htc.dto.OrderDTO;
import isd.be.htc.dto.OrderDetailsDTO;
import isd.be.htc.dto.OrderRequest;
import isd.be.htc.model.Order;
import isd.be.htc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        try {
            Order updatedOrder = orderService.updateOrder(id, orderDetails);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);

        List<OrderDetailsDTO> orderDetailDTOs = order.getOrderDetails().stream()
                .map(od -> new OrderDetailsDTO(
                        od.getProduct().getId(),
                        od.getProduct().getName(),
                        od.getQuantity(),
                        od.getUnitPrice()
                ))
                .collect(Collectors.toList());

        OrderDTO orderDTO = new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                order.getTotalAmount(),
                order.getOrderTime(),
                orderDetailDTOs
        );

        return ResponseEntity.ok(orderDTO);
    }
}
