package com.data.salesanalytics.sales;

import com.data.salesanalytics.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "sale_records")
public class SaleRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate saleDate;

    @Column(nullable = false, length = 140)
    private String productName;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(nullable = false)
    private Integer unitsSold;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 80)
    private String salesChannel;

    @Column(nullable = false, length = 80)
    private String region;

    @Column(nullable = false, length = 80)
    private String customerSegment;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        if (unitsSold != null && unitPrice != null) {
            totalAmount = unitPrice.multiply(BigDecimal.valueOf(unitsSold));
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getUnitsSold() { return unitsSold; }
    public void setUnitsSold(Integer unitsSold) { this.unitsSold = unitsSold; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getSalesChannel() { return salesChannel; }
    public void setSalesChannel(String salesChannel) { this.salesChannel = salesChannel; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getCustomerSegment() { return customerSegment; }
    public void setCustomerSegment(String customerSegment) { this.customerSegment = customerSegment; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
