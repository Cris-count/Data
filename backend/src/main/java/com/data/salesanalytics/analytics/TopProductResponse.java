package com.data.salesanalytics.analytics;

import java.math.BigDecimal;

public record TopProductResponse(
        String productName,
        BigDecimal revenue,
        long units
) {}
