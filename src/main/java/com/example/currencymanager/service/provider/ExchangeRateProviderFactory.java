package com.example.currencymanager.service.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateProviderFactory {

    private final FixerApiService fixerApiService;
    private final ExchangeRatesApiService exchangeRatesApiService;
    private final CurrencyLayerApiService currencyLayerApiService;

    @Value("${exchange.provider}")
    private String provider;

    public ExchangeRateProviderFactory(FixerApiService fixerApiService,
                                       ExchangeRatesApiService exchangeRatesApiService,
                                       CurrencyLayerApiService currencyLayerApiService) {
        this.fixerApiService = fixerApiService;
        this.exchangeRatesApiService = exchangeRatesApiService;
        this.currencyLayerApiService = currencyLayerApiService;
    }

    public ExchangeRateProvider getProvider() {
        return switch (provider.toLowerCase()) {
            case "fixer" -> fixerApiService;
            case "exchangeratesapi" -> exchangeRatesApiService;
            case "currencylayer" -> currencyLayerApiService;
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }

}
