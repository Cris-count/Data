package com.data.salesanalytics.analytics;

import java.math.BigDecimal;

public record MonthlySalesResponse(
        String month,
        BigDecimal revenue,
        long units
) {}
