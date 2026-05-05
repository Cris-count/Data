package com.data.salesanalytics.sales;

import com.data.salesanalytics.exception.ResourceNotFoundException;
import com.data.salesanalytics.user.User;
import com.data.salesanalytics.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SaleRecordService {
    private final SaleRecordRepository saleRecordRepository;
    private final UserRepository userRepository;

    public SaleRecordService(SaleRecordRepository saleRecordRepository, UserRepository userRepository) {
        this.saleRecordRepository = saleRecordRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SaleRecordResponse create(String email, SaleRecordRequest request) {
        User user = getUser(email);
        SaleRecord sale = new SaleRecord();
        apply(request, sale);
        sale.setUser(user);
        return SaleRecordResponse.from(saleRecordRepository.save(sale));
    }

    @Transactional(readOnly = true)
    public List<SaleRecordResponse> list(String email, String category, String region, String channel, LocalDate from, LocalDate to) {
        User user = getUser(email);
        String categoryFilter = blankToNull(category);
        String regionFilter = blankToNull(region);
        String channelFilter = blankToNull(channel);
        return saleRecordRepository.findByUserOrderBySaleDateDesc(user)
                .stream()
                .filter(sale -> matches(sale.getCategory(), categoryFilter))
                .filter(sale -> matches(sale.getRegion(), regionFilter))
                .filter(sale -> matches(sale.getSalesChannel(), channelFilter))
                .filter(sale -> from == null || !sale.getSaleDate().isBefore(from))
                .filter(sale -> to == null || !sale.getSaleDate().isAfter(to))
                .map(SaleRecordResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public SaleRecordResponse get(String email, Long id) {
        return SaleRecordResponse.from(getSale(email, id));
    }

    @Transactional
    public SaleRecordResponse update(String email, Long id, SaleRecordRequest request) {
        SaleRecord sale = getSale(email, id);
        apply(request, sale);
        return SaleRecordResponse.from(saleRecordRepository.save(sale));
    }

    @Transactional
    public void delete(String email, Long id) {
        saleRecordRepository.delete(getSale(email, id));
    }

    private SaleRecord getSale(String email, Long id) {
        User user = getUser(email);
        return saleRecordRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private void apply(SaleRecordRequest request, SaleRecord sale) {
        sale.setSaleDate(request.saleDate());
        sale.setProductName(request.productName().trim());
        sale.setCategory(request.category().trim());
        sale.setUnitsSold(request.unitsSold());
        sale.setUnitPrice(request.unitPrice());
        sale.setSalesChannel(request.salesChannel().trim());
        sale.setRegion(request.region().trim());
        sale.setCustomerSegment(request.customerSegment().trim());
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private boolean matches(String field, String filter) {
        return filter == null || field.equalsIgnoreCase(filter);
    }
}
