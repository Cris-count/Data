package com.data.salesanalytics.analytics;

import com.data.salesanalytics.exception.ResourceNotFoundException;
import com.data.salesanalytics.sales.SaleRecord;
import com.data.salesanalytics.sales.SaleRecordRepository;
import com.data.salesanalytics.user.User;
import com.data.salesanalytics.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SalesAnalyticsService {
    private final SaleRecordRepository saleRecordRepository;
    private final UserRepository userRepository;

    public SalesAnalyticsService(SaleRecordRepository saleRecordRepository, UserRepository userRepository) {
        this.saleRecordRepository = saleRecordRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public SalesSummaryResponse summary(String email) {
        List<SaleRecord> sales = sales(email);
        BigDecimal totalRevenue = sumRevenue(sales);
        long totalUnits = sumUnits(sales);
        BigDecimal averageTicket = sales.isEmpty()
                ? BigDecimal.ZERO
                : totalRevenue.divide(BigDecimal.valueOf(sales.size()), 2, RoundingMode.HALF_UP);
        return new SalesSummaryResponse(
                sales.size(),
                totalRevenue,
                totalUnits,
                averageTicket,
                topByUnits(sales, SaleRecord::getProductName),
                topByRevenue(sales, SaleRecord::getCategory),
                topByRevenue(sales, SaleRecord::getRegion),
                topByRevenue(sales, SaleRecord::getSalesChannel)
        );
    }

    @Transactional(readOnly = true)
    public List<MonthlySalesResponse> monthlySales(String email) {
        Map<YearMonth, List<SaleRecord>> grouped = sales(email).stream()
                .collect(Collectors.groupingBy(s -> YearMonth.from(s.getSaleDate()), LinkedHashMap::new, Collectors.toList()));
        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new MonthlySalesResponse(entry.getKey().toString(), sumRevenue(entry.getValue()), sumUnits(entry.getValue())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategorySalesResponse> byCategory(String email) {
        return grouped(email, SaleRecord::getCategory).entrySet().stream()
                .map(e -> new CategorySalesResponse(e.getKey(), sumRevenue(e.getValue()), sumUnits(e.getValue())))
                .sorted(Comparator.comparing(CategorySalesResponse::revenue).reversed())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RegionSalesResponse> byRegion(String email) {
        return grouped(email, SaleRecord::getRegion).entrySet().stream()
                .map(e -> new RegionSalesResponse(e.getKey(), sumRevenue(e.getValue()), sumUnits(e.getValue())))
                .sorted(Comparator.comparing(RegionSalesResponse::revenue).reversed())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TopProductResponse> topProducts(String email) {
        return grouped(email, SaleRecord::getProductName).entrySet().stream()
                .map(e -> new TopProductResponse(e.getKey(), sumRevenue(e.getValue()), sumUnits(e.getValue())))
                .sorted(Comparator.comparing(TopProductResponse::units).reversed())
                .limit(10)
                .toList();
    }

    private Map<String, List<SaleRecord>> grouped(String email, Function<SaleRecord, String> classifier) {
        return sales(email).stream().collect(Collectors.groupingBy(classifier));
    }

    private List<SaleRecord> sales(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return saleRecordRepository.findByUserOrderBySaleDateDesc(user);
    }

    private BigDecimal sumRevenue(List<SaleRecord> sales) {
        return sales.stream().map(SaleRecord::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private long sumUnits(List<SaleRecord> sales) {
        return sales.stream().mapToLong(SaleRecord::getUnitsSold).sum();
    }

    private String topByUnits(List<SaleRecord> sales, Function<SaleRecord, String> classifier) {
        return sales.stream()
                .collect(Collectors.groupingBy(classifier, Collectors.summingLong(SaleRecord::getUnitsSold)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sin datos");
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
