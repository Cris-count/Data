package com.data.salesanalytics.analytics;

import java.math.BigDecimal;

public record SalesSummaryResponse(
        long totalSales,
        BigDecimal totalRevenue,
        long totalUnits,
        BigDecimal averageTicket,
        String topProduct,
        String mostProfitableCategory,
        String bestRegion,
        String strongestChannel
) {}
