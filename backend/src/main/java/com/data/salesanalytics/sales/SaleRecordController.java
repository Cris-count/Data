package com.data.salesanalytics.sales;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleRecordController {
    private final SaleRecordService saleRecordService;

    public SaleRecordController(SaleRecordService saleRecordService) {
        this.saleRecordService = saleRecordService;
    }

    @PostMapping
    public SaleRecordResponse create(Authentication authentication, @Valid @RequestBody SaleRecordRequest request) {
        return saleRecordService.create(authentication.getName(), request);
    }

    @GetMapping
    public List<SaleRecordResponse> list(
            Authentication authentication,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String region,
            @RequestParam(required = false, name = "channel") String channel,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return saleRecordService.list(authentication.getName(), category, region, channel, from, to);
    }

    @GetMapping("/{id}")
    public SaleRecordResponse get(Authentication authentication, @PathVariable Long id) {
        return saleRecordService.get(authentication.getName(), id);
    }

    @PutMapping("/{id}")
    public SaleRecordResponse update(Authentication authentication, @PathVariable Long id,
                                     @Valid @RequestBody SaleRecordRequest request) {
        return saleRecordService.update(authentication.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(Authentication authentication, @PathVariable Long id) {
        saleRecordService.delete(authentication.getName(), id);
    }
}
