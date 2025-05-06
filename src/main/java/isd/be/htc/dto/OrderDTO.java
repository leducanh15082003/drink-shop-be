package isd.be.htc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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

}
