package isd.be.htc.controller;

import isd.be.htc.dto.StatisticDTO;
import isd.be.htc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/statistics")
public class DashboardController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/orders")
    public StatisticDTO getTotalOrders() {
        return orderService.getOrderStats();
    }
}
