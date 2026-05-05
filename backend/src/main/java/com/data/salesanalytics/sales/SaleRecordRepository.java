package com.data.salesanalytics.sales;

import com.data.salesanalytics.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleRecordRepository extends JpaRepository<SaleRecord, Long> {
    Optional<SaleRecord> findByIdAndUser(Long id, User user);
    List<SaleRecord> findByUserOrderBySaleDateDesc(User user);
}
