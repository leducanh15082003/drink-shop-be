package isd.be.htc.service.impl;

import isd.be.htc.config.security.CustomUserDetails;
import isd.be.htc.model.*;
import isd.be.htc.repository.*;
import isd.be.htc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final InventoryRepository inventoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository, InventoryRepository inventoryRepository, CartItemRepository cartItemRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.inventoryRepository = inventoryRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder() {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated!");
        }
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();
        Long userId = user.getId();

        // Lấy giỏ hàng của người dùng
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        // Tạo đối tượng Order và lưu vào database
        Order order = new Order();
        order.setUser(user);
        order.setOrderTime(LocalDateTime.now());
        orderRepository.save(order); // Lưu order vào database

        double totalAmount = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();

        // Duyệt qua các items trong giỏ hàng và tạo các OrderDetail
        for (CartItem cartItem : cart.getItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order); // Đảm bảo rằng OrderDetail liên kết với Order
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setUnitPrice(cartItem.getProduct().getPrice());
            orderDetails.add(orderDetail);

            totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();

            // Kiểm tra tồn kho
            Inventory inventory = inventoryRepository.findByProduct(cartItem.getProduct());
            if (inventory == null || inventory.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Inventory is not enough!");
            }
            inventory.setQuantity(inventory.getQuantity() - cartItem.getQuantity());
            inventoryRepository.save(inventory);
        }

        // Cập nhật tổng tiền và thiết lập các OrderDetails vào Order
        order.setTotalAmount(totalAmount);
        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails); // Đảm bảo OrderDetails đã được thêm vào

        // Lưu Order với các OrderDetails
        orderRepository.save(order); // Hibernate sẽ tự động lưu các OrderDetails nhờ CascadeType.ALL

        // Sau khi đơn hàng được tạo, xóa các item trong giỏ hàng và giỏ hàng
        cartItemRepository.deleteAll(cart.getItems());
        cartRepository.delete(cart);

        return order; // Trả về đối tượng Order vừa tạo
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
