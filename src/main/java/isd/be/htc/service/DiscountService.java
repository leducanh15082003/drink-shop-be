package isd.be.htc.service;

import isd.be.htc.dto.DiscountDTO;
import isd.be.htc.model.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountService {
    List<Discount> getAllDiscounts();

    Optional<Discount> getDiscountById(Long id);

    Discount createDiscount(DiscountDTO discount);

    Discount updateDiscount(Long id, DiscountDTO discountDetails);

    void updateDiscountStatus(Long id, boolean isActive);

    void deleteDiscount(Long id);
}
