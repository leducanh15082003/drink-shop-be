package isd.be.htc.dto;

import java.time.LocalDateTime;

import isd.be.htc.model.enums.DiscountAmountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {
    private String code;

    private DiscountAmountType discountAmountType;

    private Double amount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer quantity;

    private Double minimumOrderPrice;

    private Boolean isActive;

    private String description;
}
