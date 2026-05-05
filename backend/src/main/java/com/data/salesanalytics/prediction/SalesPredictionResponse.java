package com.data.salesanalytics.prediction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record SalesPredictionResponse(
        Long id,
        LocalDate predictionDate,
        String targetMonth,
        BigDecimal predictedRevenue,
        Long predictedUnits,
        TrendLevel trendLevel,
        ConfidenceLevel confidenceLevel,
        String predictionMessage,
        String recommendedAction,
        Instant createdAt
) {
    public static SalesPredictionResponse from(SalesPrediction prediction) {
        return new SalesPredictionResponse(
                prediction.getId(),
                prediction.getPredictionDate(),
                prediction.getTargetMonth().toString(),
                prediction.getPredictedRevenue(),
                prediction.getPredictedUnits(),
                prediction.getTrendLevel(),
                prediction.getConfidenceLevel(),
                prediction.getPredictionMessage(),
                prediction.getRecommendedAction(),
                prediction.getCreatedAt()
        );
    }
}
