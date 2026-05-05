package com.data.salesanalytics.prediction;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predictions/sales")
public class SalesPredictionController {
    private final SalesPredictionService salesPredictionService;

    public SalesPredictionController(SalesPredictionService salesPredictionService) {
        this.salesPredictionService = salesPredictionService;
    }

    @PostMapping("/next-month")
    public SalesPredictionResponse generateNextMonth(Authentication authentication) {
        return salesPredictionService.generateNextMonth(authentication.getName());
    }

    @GetMapping
    public List<SalesPredictionResponse> list(Authentication authentication) {
        return salesPredictionService.list(authentication.getName());
    }

    @GetMapping("/{id}")
    public SalesPredictionResponse get(Authentication authentication, @PathVariable Long id) {
        return salesPredictionService.get(authentication.getName(), id);
    }

    @DeleteMapping("/{id}")
    public void delete(Authentication authentication, @PathVariable Long id) {
        salesPredictionService.delete(authentication.getName(), id);
    }
}
