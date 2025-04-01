package isd.be.htc.service;

import isd.be.htc.model.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {
    List<OrderDetail> getAllOrderDetails();
    Optional<OrderDetail> getOrderDetailById(Long id);
    List<OrderDetail> getOrderDetailsByOrder(Long orderId);
    OrderDetail createOrderDetail(OrderDetail orderDetail);
    OrderDetail updateOrderDetail(Long id, OrderDetail orderDetailDetails);
    void deleteOrderDetail(Long id);
}
