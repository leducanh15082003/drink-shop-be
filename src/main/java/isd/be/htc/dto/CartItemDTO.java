package isd.be.htc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private double unitPrice;

    // 🆕 Các tùy chọn khách chọn
    private String size;
    private String sugarRate;
    private String iceRate;

    public CartItemDTO(Long productId, String productName, int quantity, double unitPrice,
            String size, String sugarRate, String iceRate) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.size = size;
        this.sugarRate = sugarRate;
        this.iceRate = iceRate;
    }

    public CartItemDTO() {
    }
}
