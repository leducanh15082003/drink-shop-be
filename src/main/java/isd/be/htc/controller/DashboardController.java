package isd.be.htc.controller;

import isd.be.htc.dto.MonthlyRevenueDTO;
import isd.be.htc.dto.StatisticDTO;
import isd.be.htc.service.OrderService;
import isd.be.htc.service.ProductService;
import isd.be.htc.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics")
public class DashboardController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private VisitService visitService;

    @GetMapping("/orders")
    public StatisticDTO getTotalOrders() {
        return orderService.getOrderStats();
    }

    @GetMapping("/revenue")
    public StatisticDTO getMonthlyRevenue() {
        return orderService.getMonthlyRevenue();
    }

    @GetMapping("/total-products")
    public StatisticDTO getTotalProductsStat() {
        return productService.getTotalProductsStat();
    }

    @GetMapping("visit-counts")
    public StatisticDTO getVisits() {
        return visitService.getMonthlyVisitStat();
    }

    @GetMapping("/revenue-data")
    public List<MonthlyRevenueDTO> getRevenueDataEachMonth(@RequestParam(name = "year", defaultValue = "2025") int year) {
        return orderService.getMonthlyRevenueData(year);
    }
}
