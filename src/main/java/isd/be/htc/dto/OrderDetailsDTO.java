package isd.be.htc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailsDTO {
    private String productName;
    private String size;
    private String sugarRate;
    private String iceRate;
    private int quantity;
    private double unitPrice;

    public OrderDetailsDTO(String productName, String size, String sugarRate, String iceRate, int quantity,
            double unitPrice) {
        this.productName = productName;
        this.size = size;
        this.sugarRate = sugarRate;
        this.iceRate = iceRate;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
