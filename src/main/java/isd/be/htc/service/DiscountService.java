package isd.be.htc.service;

import isd.be.htc.model.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountService {
    List<Discount> getAllDiscounts();
    Optional<Discount> getDiscountById(Long id);
    Discount createDiscount(Discount discount);
    Discount updateDiscount(Long id, Discount discountDetails);
    void deleteDiscount(Long id);
}
