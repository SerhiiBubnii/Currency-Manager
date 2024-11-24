package com.example.currencymanager.service.provider;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExchangeRatesApiService implements ExchangeRateProvider {

    private final WebClient exchangeRatesWebClient;

    @Value("${exchangeratesapi.api.key}")
    private String apiKey;

    public ExchangeRatesApiService(WebClient exchangeRatesWebClient) {
        this.exchangeRatesWebClient = exchangeRatesWebClient;
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates(String baseCurrency) {
        return exchangeRatesWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("access_key", apiKey)
                        .queryParam("base", baseCurrency)
                        .build())
                .retrieve()
                .bodyToMono(ExchangeRatesApiResponse.class)
                .block()
                .getRates();
    }

    @Getter
    @Setter
    static class ExchangeRatesApiResponse {

        private Map<String, BigDecimal> rates;

    }

}
