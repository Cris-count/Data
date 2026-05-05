package com.data.salesanalytics.analytics;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class SalesAnalyticsController {
    private final SalesAnalyticsService salesAnalyticsService;

    public SalesAnalyticsController(SalesAnalyticsService salesAnalyticsService) {
        this.salesAnalyticsService = salesAnalyticsService;
    }

    @GetMapping("/summary")
    public SalesSummaryResponse summary(Authentication authentication) {
        return salesAnalyticsService.summary(authentication.getName());
    }

    @GetMapping("/monthly-sales")
    public List<MonthlySalesResponse> monthlySales(Authentication authentication) {
        return salesAnalyticsService.monthlySales(authentication.getName());
    }

    @GetMapping("/by-category")
    public List<CategorySalesResponse> byCategory(Authentication authentication) {
        return salesAnalyticsService.byCategory(authentication.getName());
    }

    @GetMapping("/by-region")
    public List<RegionSalesResponse> byRegion(Authentication authentication) {
        return salesAnalyticsService.byRegion(authentication.getName());
    }

    @GetMapping("/top-products")
    public List<TopProductResponse> topProducts(Authentication authentication) {
        return salesAnalyticsService.topProducts(authentication.getName());
    }
}
