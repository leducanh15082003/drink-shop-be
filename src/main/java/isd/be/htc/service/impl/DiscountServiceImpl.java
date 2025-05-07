package isd.be.htc.service.impl;

import isd.be.htc.dto.DiscountDTO;
import isd.be.htc.model.Discount;
import isd.be.htc.repository.DiscountRepository;
import isd.be.htc.repository.OrderRepository;
import isd.be.htc.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository, OrderRepository orderRepository) {
        this.discountRepository = discountRepository;
        this.orderRepository = orderRepository;
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
    public Discount createDiscount(DiscountDTO discount) {
        if (discountRepository.existsByCode(discount.getCode())) {
            throw new RuntimeException("Discount code already exists");
        }

        Discount newDiscount = new Discount();
        newDiscount.setCode(discount.getCode());
        newDiscount.setDiscountAmountType(discount.getDiscountAmountType());
        newDiscount.setAmount(discount.getAmount());
        newDiscount.setStartDate(discount.getStartDate());
        newDiscount.setEndDate(discount.getEndDate());
        newDiscount.setQuantity(discount.getQuantity());
        newDiscount.setMinimumOrderPrice(discount.getMinimumOrderPrice());
        newDiscount.setDescription(discount.getDescription());
        newDiscount.setIsActive(true);
        return discountRepository.save(newDiscount);
    }

    @Override
    public Discount updateDiscount(Long id, DiscountDTO discountDetails) {
        return discountRepository.findById(id).map(discount -> {
            discount.setCode(discountDetails.getCode());
            discount.setDiscountAmountType(discountDetails.getDiscountAmountType());
            discount.setAmount(discountDetails.getAmount());
            discount.setStartDate(discountDetails.getStartDate());
            discount.setEndDate(discountDetails.getEndDate());
            discount.setQuantity(discountDetails.getQuantity());
            discount.setMinimumOrderPrice(discountDetails.getMinimumOrderPrice());
            discount.setDescription(discountDetails.getDescription());
            discount.setIsActive(discountDetails.getIsActive());
            return discountRepository.save(discount);
        }).orElseThrow(() -> new RuntimeException("Discount not found"));
    }

    @Override
    @Transactional
    public void deleteDiscount(Long id) {
        orderRepository.clearDiscountFromOrders(id);
        discountRepository.deleteById(id);
    }

    @Override
    public void updateDiscountStatus(Long id, boolean isActive) {
        discountRepository.findById(id).ifPresent(discount -> {
            discount.setIsActive(isActive);
            discountRepository.save(discount);
        });
    }

    @Override
    public List<Discount> getAllActiveDiscounts() {
        return discountRepository.findValidDiscounts();
    }
}
