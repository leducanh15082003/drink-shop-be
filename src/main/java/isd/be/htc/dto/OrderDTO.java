package isd.be.htc.dto;

import isd.be.htc.model.Order;
import isd.be.htc.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import isd.be.htc.model.enums.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private String userName;
    private double price;
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;
    private PaymentDTO payment;
    private List<OrderDetailsDTO> orderDetails;
    private String address;
    private String phoneNumber;
    private Double discountAmDouble;

    public static OrderDTO fromEntity(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());

        User user = order.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setUserName(user.getFullName());
        }

        dto.setPrice(order.getTotalAmount());
        dto.setOrderTime(order.getOrderTime());
        dto.setOrderStatus(order.getStatus() != null ? order.getStatus() : null);
        dto.setAddress(order.getAddress());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setDiscountAmDouble(order.getDiscountAmount());

        if (order.getPayment() != null) {
            dto.setPayment(PaymentDTO.fromEntity(order.getPayment()));
        }

        dto.setOrderDetails(order.getOrderDetails()
                .stream()
                .map(OrderDetailsDTO::fromEntity)
                .collect(Collectors.toList())
        );

        return dto;
    }
}
