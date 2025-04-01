package isd.be.htc.service.impl;

import isd.be.htc.model.OrderDetail;
import isd.be.htc.repository.OrderDetailRepository;
import isd.be.htc.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    @Override
    public Optional<OrderDetail> getOrderDetailById(Long id) {
        return orderDetailRepository.findById(id);
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrder(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetail orderDetailDetails) {
        return orderDetailRepository.findById(id).map(orderDetail -> {
            orderDetail.setOrder(orderDetailDetails.getOrder());
            orderDetail.setProduct(orderDetailDetails.getProduct());
            orderDetail.setQuantity(orderDetailDetails.getQuantity());
            orderDetail.setUnitPrice(orderDetailDetails.getUnitPrice());
            return orderDetailRepository.save(orderDetail);
        }).orElseThrow(() -> new RuntimeException("OrderDetail not found!"));
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
