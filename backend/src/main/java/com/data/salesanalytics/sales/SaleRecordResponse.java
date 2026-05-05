package com.data.salesanalytics.sales;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record SaleRecordResponse(
        Long id,
        LocalDate saleDate,
        String productName,
        String category,
        Integer unitsSold,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        String salesChannel,
        String region,
        String customerSegment,
        Instant createdAt
) {
    public static SaleRecordResponse from(SaleRecord sale) {
        return new SaleRecordResponse(
                sale.getId(),
                sale.getSaleDate(),
                sale.getProductName(),
                sale.getCategory(),
                sale.getUnitsSold(),
                sale.getUnitPrice(),
                sale.getTotalAmount(),
                sale.getSalesChannel(),
                sale.getRegion(),
                sale.getCustomerSegment(),
                sale.getCreatedAt()
        );
    }
}
