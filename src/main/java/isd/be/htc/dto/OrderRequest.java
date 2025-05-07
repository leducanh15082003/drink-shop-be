package isd.be.htc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private List<CartItemDTO> items;
    private double totalPrice;
    private String address;
    private String phoneNumber;
    private String paymentMethod; // Thêm để khởi tạo Payment
    private Long discountId;
    private double discountAmount;
    private String note;
}
