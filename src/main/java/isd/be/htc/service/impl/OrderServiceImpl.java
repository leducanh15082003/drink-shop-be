package isd.be.htc.service.impl;

import isd.be.htc.config.security.CustomUserDetails;
import isd.be.htc.dto.*;
import isd.be.htc.model.*;
import isd.be.htc.model.enums.DiscountAmountType;
import isd.be.htc.model.enums.LoyaltyMember;
import isd.be.htc.model.enums.OrderStatus;
import isd.be.htc.repository.*;
import isd.be.htc.service.OrderService;
import isd.be.htc.service.SupabaseNotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final UserRepository userRepository;
    private final SupabaseNotificationService notificationService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository,
            DiscountRepository discountRepository,
            UserRepository userRepository,
            SupabaseNotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.userRepository = userRepository;
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

            Long userId = null;
            if (order.getUser() != null) {
                userId = order.getUser().getId();
            }

            return new OrderDTO(
                    order.getId(),
                    userId,
                    order.getUser() != null ? order.getUser().getFullName() : null,
                    order.getTotalAmount(),
                    order.getOrderTime(),
                    order.getStatus(),
                    paymentDTO,
                    detailsDTO,
                    order.getAddress(),
                    order.getPhoneNumber(),
                    order.getDiscountAmount(),
                    order.getNote());
        }).toList();
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return OrderDTO.fromEntity(order);
    }

    @Transactional
    @Override
    public Order createOrder(OrderRequest orderRequest) {
        try {
            User user = null;
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
                user = userDetails.getUser();

                long count = orderRepository.countByUserId(user.getId());
                boolean updated = false;

                if (count > 10 && user.getLoyaltyMember() != LoyaltyMember.LEVEL_1) {
                    user.setLoyaltyMember(LoyaltyMember.LEVEL_1);
                    updated = true;
                }

                if (count > 50 && user.getLoyaltyMember() != LoyaltyMember.LEVEL_2) {
                    user.setLoyaltyMember(LoyaltyMember.LEVEL_2);
                    updated = true;
                }

                if (updated) {
                    userRepository.save(user); // ✅ Lưu vào DB
                }
            }

            Order order = new Order();
            if (orderRequest.getDiscountId() != null) {
                Discount discount = discountRepository.findById(orderRequest.getDiscountId())
                        .orElseThrow(() -> new RuntimeException("Discount not found"));

                discount.setQuantity(discount.getQuantity() - 1);

                order.setDiscount(discount);
            }
            order.setOrderTime(LocalDateTime.now());
            order.setStatus(OrderStatus.PENDING);
            order.setTotalAmount(orderRequest.getTotalPrice());
            order.setUser(user); // Có thể null
            order.setAddress(orderRequest.getAddress());
            order.setPhoneNumber(orderRequest.getPhoneNumber());
            order.setNote(orderRequest.getNote());
            order.setDiscountAmount(orderRequest.getDiscountAmount());

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

                product.setQuantitySold(product.getQuantitySold() + item.getQuantity());

                productRepository.save(product);

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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
                    order.getUser().getFullName(),
                    order.getTotalAmount(),
                    order.getOrderTime(),
                    order.getStatus(),
                    paymentDTO,
                    detailsDTO,
                    order.getAddress(),
                    order.getPhoneNumber(),
                    order.getDiscountAmount(),
                    order.getNote());
        }).toList();
    }

    @Override
    public StatisticDTO getOrderStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfThisMonth = now.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
        LocalDateTime startOfNextMonth = startOfThisMonth.plusMonths(1);

        LocalDateTime startOfLastMonth = startOfThisMonth.minusMonths(1);
        LocalDateTime endOfLastMonth = startOfThisMonth;

        long currentCount = orderRepository.countByStatusAndOrderTimeBetween(OrderStatus.COMPLETED, startOfThisMonth,
                startOfNextMonth);
        long previousCount = orderRepository.countByStatusAndOrderTimeBetween(OrderStatus.COMPLETED, startOfLastMonth,
                endOfLastMonth);

        double changePercentage = 0;
        if (previousCount > 0) {
            changePercentage = ((double) (currentCount - previousCount) / previousCount) * 100;
        } else {
            changePercentage = 0;
        }

        String changeString = String.format("%s%.1f%%", changePercentage >= 0 ? "+" : "", changePercentage);
        boolean positive = changePercentage >= 0;

        return new StatisticDTO("Total Orders", currentCount, changeString, positive);
    }

    @Override
    public StatisticDTO getMonthlyRevenue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfThisMonth = now.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
        LocalDateTime startOfNextMonth = startOfThisMonth.plusMonths(1);

        LocalDateTime startOfLastMonth = startOfThisMonth.minusMonths(1);
        LocalDateTime endOfLastMonth = startOfThisMonth;

        Double thisMonthSum = orderRepository.sumTotalAmountByStatusBetween(OrderStatus.COMPLETED, startOfThisMonth,
                startOfNextMonth);
        Double lastMonthSum = orderRepository.sumTotalAmountByStatusBetween(OrderStatus.COMPLETED, startOfLastMonth,
                endOfLastMonth);

        double current = (thisMonthSum != null ? thisMonthSum : 0);
        double previous = (lastMonthSum != null ? lastMonthSum : 0);

        double changePercentage = 0;
        if (previous > 0) {
            changePercentage = ((current - previous) / previous) * 100;
        }

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String revenueString = format.format(current);

        String changeString = String.format("%+.1f%%", changePercentage);
        boolean positive = changePercentage >= 0;

        return new StatisticDTO("Revenue", revenueString, changeString, positive);
    }

    public List<MonthlyRevenueDTO> getMonthlyRevenueData(int year) {
        Map<String, Double> revenueMap = orderRepository.findMonthlyRevenueRaw(OrderStatus.COMPLETED.name(), year)
                .stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> ((Number) arr[1]).doubleValue()));

        List<String> allMonths = Arrays.asList(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        return allMonths.stream()
                .map(month -> new MonthlyRevenueDTO(month, revenueMap.getOrDefault(month, 0.0)))
                .collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found!"));
        order.setStatus(status);
        orderRepository.save(order);
    }
}
