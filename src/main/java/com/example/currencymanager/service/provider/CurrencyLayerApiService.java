package com.example.currencymanager.service.provider;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyLayerApiService implements ExchangeRateProvider {

    private final WebClient currencyLayerWebClient;

    @Value("${currencylayer.api.key}")
    private String apiKey;

    public CurrencyLayerApiService(WebClient currencyLayerWebClient) {
        this.currencyLayerWebClient = currencyLayerWebClient;
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates(String baseCurrency) {
        return currencyLayerWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/live")
                        .queryParam("access_key", apiKey)
                        .queryParam("source", baseCurrency)
                        .build())
                .retrieve()
                .bodyToMono(CurrencyLayerResponse.class)
                .block()
                .getQuotes();
    }

    @Getter
    @Setter
    static class CurrencyLayerResponse {

        private Map<String, BigDecimal> quotes;

    }

}
