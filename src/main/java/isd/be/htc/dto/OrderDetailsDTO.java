package isd.be.htc.dto;

import isd.be.htc.model.OrderDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

    public static OrderDetailsDTO fromEntity(OrderDetail od) {
        OrderDetailsDTO dto = new OrderDetailsDTO();
        dto.setProductName(od.getProduct().getName());
        dto.setSize(od.getSize());
        dto.setSugarRate(od.getSugarRate());
        dto.setIceRate(od.getIceRate());
        dto.setQuantity(od.getQuantity());
        dto.setUnitPrice(od.getUnitPrice());
        return dto;
    }
}
