package com.data.salesanalytics.analytics;

import java.math.BigDecimal;

public record RegionSalesResponse(
        String region,
        BigDecimal revenue,
        long units
) {}
