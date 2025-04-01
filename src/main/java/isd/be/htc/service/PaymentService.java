package isd.be.htc.service;

import isd.be.htc.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    List<Payment> getAllPayments();
    Optional<Payment> getPaymentById(Long id);
    Optional<Payment> getPaymentByOrderId(Long orderId);
    Payment createPayment(Payment payment);
    Payment updatePayment(Long id, Payment paymentDetails);
    void deletePayment(Long id);
}
