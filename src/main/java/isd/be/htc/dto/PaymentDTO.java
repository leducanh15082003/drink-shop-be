package isd.be.htc.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Double amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime transactionDate;
}
