package isd.be.htc.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import isd.be.htc.model.enums.OrderStatus;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private Long userId;
    private double price;
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;
    private PaymentDTO payment;
    private List<OrderDetailsDTO> orderDetails;
    private String address;
    private String phoneNumber;

    public OrderDTO(Long id, Long userId, double price, LocalDateTime orderTime, OrderStatus orderStatus,
            PaymentDTO payment,
            List<OrderDetailsDTO> orderDetails, String address, String phoneNumber) {
        this.id = id;
        this.userId = userId;
        this.price = price;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.payment = payment;
        this.orderDetails = orderDetails;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
