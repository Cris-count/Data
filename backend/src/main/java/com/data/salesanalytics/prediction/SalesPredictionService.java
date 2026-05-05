package com.data.salesanalytics.prediction;

import com.data.salesanalytics.exception.ResourceNotFoundException;
import com.data.salesanalytics.sales.SaleRecord;
import com.data.salesanalytics.sales.SaleRecordRepository;
import com.data.salesanalytics.user.User;
import com.data.salesanalytics.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SalesPredictionService {
    private final SalesPredictionRepository salesPredictionRepository;
    private final SaleRecordRepository saleRecordRepository;
    private final UserRepository userRepository;

    public SalesPredictionService(SalesPredictionRepository salesPredictionRepository,
                                  SaleRecordRepository saleRecordRepository,
                                  UserRepository userRepository) {
        this.salesPredictionRepository = salesPredictionRepository;
        this.saleRecordRepository = saleRecordRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SalesPredictionResponse generateNextMonth(String email) {
        User user = user(email);
        List<SaleRecord> sales = saleRecordRepository.findByUserOrderBySaleDateDesc(user);
        Map<YearMonth, List<SaleRecord>> byMonth = sales.stream()
                .collect(Collectors.groupingBy(s -> YearMonth.from(s.getSaleDate())));

        YearMonth lastMonth = byMonth.keySet().stream().max(Comparator.naturalOrder()).orElse(YearMonth.now());
        YearMonth targetMonth = lastMonth.plusMonths(1);
        BigDecimal averageRevenue = averageRevenue(byMonth);
        BigDecimal lastRevenue = sumRevenue(byMonth.getOrDefault(lastMonth, List.of()));
        long averageUnits = averageUnits(byMonth);
        long lastUnits = sumUnits(byMonth.getOrDefault(lastMonth, List.of()));
        BigDecimal variation = calculateVariation(lastRevenue, averageRevenue);

        TrendLevel trend = classifyTrend(byMonth.size(), variation);
        ConfidenceLevel confidence = classifyConfidence(sales.size(), byMonth.size());
        BigDecimal adjustment = BigDecimal.ONE.add(variation.divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP));
        if (adjustment.compareTo(BigDecimal.valueOf(0.4)) < 0) {
            adjustment = BigDecimal.valueOf(0.4);
        }
        BigDecimal predictedRevenue = averageRevenue.multiply(adjustment).setScale(2, RoundingMode.HALF_UP);
        long predictedUnits = Math.max(0, Math.round(((double) averageUnits + lastUnits) / 2));

        String topCategory = topByRevenue(sales, SaleRecord::getCategory);
        String topRegion = topByRevenue(sales, SaleRecord::getRegion);
        String topChannel = topByRevenue(sales, SaleRecord::getSalesChannel);

        SalesPrediction prediction = new SalesPrediction();
        prediction.setPredictionDate(LocalDate.now());
        prediction.setTargetMonth(targetMonth);
        prediction.setPredictedRevenue(predictedRevenue);
        prediction.setPredictedUnits(predictedUnits);
        prediction.setTrendLevel(trend);
        prediction.setConfidenceLevel(confidence);
        prediction.setPredictionMessage(message(variation, trend, byMonth.size()));
        prediction.setRecommendedAction(action(trend, topCategory, topRegion, topChannel));
        prediction.setUser(user);
        return SalesPredictionResponse.from(salesPredictionRepository.save(prediction));
    }

    @Transactional(readOnly = true)
    public List<SalesPredictionResponse> list(String email) {
        return salesPredictionRepository.findByUserOrderByCreatedAtDesc(user(email)).stream()
                .map(SalesPredictionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public SalesPredictionResponse get(String email, Long id) {
        return SalesPredictionResponse.from(prediction(email, id));
    }

    @Transactional
    public void delete(String email, Long id) {
        salesPredictionRepository.delete(prediction(email, id));
    }

    private SalesPrediction prediction(String email, Long id) {
        return salesPredictionRepository.findByIdAndUser(id, user(email))
                .orElseThrow(() -> new ResourceNotFoundException("Prediccion no encontrada"));
    }

    private User user(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private BigDecimal averageRevenue(Map<YearMonth, List<SaleRecord>> byMonth) {
        if (byMonth.isEmpty()) return BigDecimal.ZERO;
        BigDecimal total = byMonth.values().stream().map(this::sumRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(byMonth.size()), 2, RoundingMode.HALF_UP);
    }

    private long averageUnits(Map<YearMonth, List<SaleRecord>> byMonth) {
        if (byMonth.isEmpty()) return 0;
        return Math.round((double) byMonth.values().stream().mapToLong(this::sumUnits).sum() / byMonth.size());
    }

    private BigDecimal calculateVariation(BigDecimal lastRevenue, BigDecimal averageRevenue) {
        if (averageRevenue.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return lastRevenue.subtract(averageRevenue).divide(averageRevenue, 4, RoundingMode.HALF_UP);
    }

    private TrendLevel classifyTrend(int monthCount, BigDecimal variation) {
        if (monthCount < 2) return TrendLevel.LOW;
        double pct = variation.doubleValue() * 100;
        if (pct < -10) return TrendLevel.DECREASING;
        if (pct <= 10) return TrendLevel.STABLE;
        if (pct <= 25) return TrendLevel.GROWING;
        return TrendLevel.HIGH_GROWTH;
    }

    private ConfidenceLevel classifyConfidence(int salesCount, int monthCount) {
        if (salesCount < 8 || monthCount < 2) return ConfidenceLevel.LOW;
        if (salesCount < 20 || monthCount < 4) return ConfidenceLevel.MEDIUM;
        return ConfidenceLevel.HIGH;
    }

    private String message(BigDecimal variation, TrendLevel trend, int monthCount) {
        if (trend == TrendLevel.LOW) {
            return "Hay pocos datos historicos; la prediccion usa un promedio simple y debe interpretarse con cautela.";
        }
        BigDecimal pct = variation.multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP);
        return "Modelo heuristico basico: el ultimo mes varia " + pct + "% frente al promedio mensual historico.";
    }

    private String action(TrendLevel trend, String category, String region, String channel) {
        return switch (trend) {
            case DECREASING -> "Revisar precios y campanas en " + channel + ", priorizando la categoria " + category + " en " + region + ".";
            case HIGH_GROWTH -> "Aumentar inventario de " + category + " y reforzar capacidad comercial en " + region + " y canal " + channel + ".";
            case GROWING -> "Mantener impulso comercial en " + channel + " y preparar inventario adicional para " + category + ".";
            case STABLE -> "Optimizar margen y fidelizacion en " + region + ", manteniendo seguimiento semanal de " + category + ".";
            case LOW -> "Registrar mas ventas historicas antes de tomar decisiones fuertes; usar esta salida como orientacion inicial.";
        };
    }

    private BigDecimal sumRevenue(List<SaleRecord> sales) {
        return sales.stream().map(SaleRecord::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private long sumUnits(List<SaleRecord> sales) {
        return sales.stream().mapToLong(SaleRecord::getUnitsSold).sum();
    }

    private String topByRevenue(List<SaleRecord> sales, Function<SaleRecord, String> classifier) {
        return sales.stream()
                .collect(Collectors.groupingBy(classifier,
                        Collectors.reducing(BigDecimal.ZERO, SaleRecord::getTotalAmount, BigDecimal::add)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sin datos");
    }
}
