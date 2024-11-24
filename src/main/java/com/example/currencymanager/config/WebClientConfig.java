package com.example.currencymanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${fixer.api.url}")
    private String fixerBaseUrl;

    @Value("${exchangeratesapi.api.url}")
    private String exchangeRatesBaseUrl;

    @Value("${currencylayer.api.url}")
    private String currencyLayerBaseUrl;

    @Bean(name = "fixerWebClient")
    public WebClient fixerWebClient() {
        return WebClient.builder()
                .baseUrl(fixerBaseUrl)
                .build();
    }

    @Bean(name = "exchangeRatesWebClient")
    public WebClient exchangeRatesWebClient() {
        return WebClient.builder()
                .baseUrl(exchangeRatesBaseUrl)
                .build();
    }

    @Bean(name = "currencyLayerWebClient")
    public WebClient currencyLayerWebClient() {
        return WebClient.builder()
                .baseUrl(currencyLayerBaseUrl)
                .build();
    }

}
