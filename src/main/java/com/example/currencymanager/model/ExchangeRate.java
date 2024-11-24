package com.example.currencymanager.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "exchange_rates")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "base_currency", nullable = false)
    private String baseCurrency;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ElementCollection
    @CollectionTable(name = "exchange_rate_values", joinColumns = @JoinColumn(name = "exchange_rate_id"))
    @MapKeyColumn(name = "currency_code")
    @Column(name = "rate")
    private Map<String, BigDecimal> rates;

}
