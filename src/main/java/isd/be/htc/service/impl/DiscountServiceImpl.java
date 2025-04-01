package isd.be.htc.service.impl;

import isd.be.htc.model.Discount;
import isd.be.htc.repository.DiscountRepository;
import isd.be.htc.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @Override
    public Optional<Discount> getDiscountById(Long id) {
        return discountRepository.findById(id);
    }

    @Override
    public Discount createDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    @Override
    public Discount updateDiscount(Long id, Discount discountDetails) {
        return discountRepository.findById(id).map(discount -> {
            discount.setDiscountPercentage(discountDetails.getDiscountPercentage());
            discount.setProduct(discountDetails.getProduct());
            discount.setStartDate(discountDetails.getStartDate());
            discount.setEndDate(discountDetails.getEndDate());
            return discountRepository.save(discount);
        }).orElseThrow(() -> new RuntimeException("Discount not found"));
    }

    @Override
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }
}
