package com.example.currencymanager.service.provider;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class FixerApiService implements ExchangeRateProvider {

    private final WebClient fixerWebClient;

    @Value("${fixer.api.key}")
    private String apiKey;

    public FixerApiService(WebClient fixerWebClient) {
        this.fixerWebClient = fixerWebClient;
    }

    public Map<String, BigDecimal> getExchangeRates(String baseCurrency) {
        return fixerWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("access_key", apiKey)
                        .queryParam("base", baseCurrency)
                        .build())
                .retrieve()
                .bodyToMono(FixerResponse.class)
                .block()
                .getRates();
    }

    @Getter
    @Setter
    static class FixerResponse {

        private Map<String, BigDecimal> rates;

    }

}
