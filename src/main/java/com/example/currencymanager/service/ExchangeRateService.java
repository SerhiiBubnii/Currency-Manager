package com.example.currencymanager.service;

import com.example.currencymanager.model.ExchangeRate;
import com.example.currencymanager.repository.ExchangeRateRepository;
import com.example.currencymanager.service.provider.ExchangeRateProviderFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Service
public class ExchangeRateService implements IExchangeRateService {

    private final Map<String, Map<String, BigDecimal>> cachedRatesByCurrency = new ConcurrentHashMap<>();

    private final ExchangeRateProviderFactory providerFactory;
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateService(ExchangeRateProviderFactory providerFactory,
                               ExchangeRateRepository exchangeRateRepository) {
        this.providerFactory = providerFactory;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public void updateRates(String baseCurrency) {
        Map<String, BigDecimal> exchangeRates = providerFactory.getProvider().getExchangeRates(baseCurrency);
        if (!exchangeRates.isEmpty()) {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setBaseCurrency(baseCurrency);
            exchangeRate.setTimestamp(now());
            exchangeRate.setRates(exchangeRates);
            exchangeRateRepository.save(exchangeRate);
            cachedRatesByCurrency.put(baseCurrency, exchangeRates);
        }
    }

    @Override
    public Map<String, BigDecimal> getRates(String currencyCode) {
        return ofNullable(cachedRatesByCurrency.get(currencyCode))
                .orElseThrow(() -> new RuntimeException("Exchange rates not found for code: " + currencyCode));
    }

}
