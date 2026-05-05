package com.data.salesanalytics.prediction;

import com.data.salesanalytics.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalesPredictionRepository extends JpaRepository<SalesPrediction, Long> {
    List<SalesPrediction> findByUserOrderByCreatedAtDesc(User user);
    Optional<SalesPrediction> findByIdAndUser(Long id, User user);
}
