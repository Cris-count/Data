package com.data.salesanalytics.prediction;

import com.data.salesanalytics.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "sales_predictions")
public class SalesPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate predictionDate;

    @Column(nullable = false, length = 7)
    private String targetMonth;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal predictedRevenue;

    @Column(nullable = false)
    private Long predictedUnits;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrendLevel trendLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConfidenceLevel confidenceLevel;

    @Column(nullable = false, length = 600)
    private String predictionMessage;

    @Column(nullable = false, length = 600)
    private String recommendedAction;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getPredictionDate() { return predictionDate; }
    public void setPredictionDate(LocalDate predictionDate) { this.predictionDate = predictionDate; }
    public YearMonth getTargetMonth() { return YearMonth.parse(targetMonth); }
    public void setTargetMonth(YearMonth targetMonth) { this.targetMonth = targetMonth.toString(); }
    public BigDecimal getPredictedRevenue() { return predictedRevenue; }
    public void setPredictedRevenue(BigDecimal predictedRevenue) { this.predictedRevenue = predictedRevenue; }
    public Long getPredictedUnits() { return predictedUnits; }
    public void setPredictedUnits(Long predictedUnits) { this.predictedUnits = predictedUnits; }
    public TrendLevel getTrendLevel() { return trendLevel; }
    public void setTrendLevel(TrendLevel trendLevel) { this.trendLevel = trendLevel; }
    public ConfidenceLevel getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(ConfidenceLevel confidenceLevel) { this.confidenceLevel = confidenceLevel; }
    public String getPredictionMessage() { return predictionMessage; }
    public void setPredictionMessage(String predictionMessage) { this.predictionMessage = predictionMessage; }
    public String getRecommendedAction() { return recommendedAction; }
    public void setRecommendedAction(String recommendedAction) { this.recommendedAction = recommendedAction; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
