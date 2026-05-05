package com.data.salesanalytics.analytics;

import java.math.BigDecimal;

public record CategorySalesResponse(
        String category,
        BigDecimal revenue,
        long units
) {}
