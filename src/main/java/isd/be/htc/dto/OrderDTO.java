package isd.be.htc.dto;

import isd.be.htc.model.OrderDetail;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private Long userId;
    private double price;
    private LocalDateTime orderTime;
    private List<OrderDetailsDTO> orderDetails;

    public OrderDTO(Long id, Long userId, double price, LocalDateTime orderTime, List<OrderDetailsDTO> orderDetails) {
        this.id = id;
        this.userId = userId;
        this.price = price;
        this.orderTime = orderTime;
        this.orderDetails = orderDetails;
    }
}
