package isd.be.htc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailsDTO {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private double unitPrice;

    public OrderDetailsDTO(Long id, Long productId, String productName, int quantity, double unitPrice) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
