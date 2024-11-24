package com.example.currencymanager.controller;

import com.example.currencymanager.repository.ExchangeRateRepository;
import com.example.currencymanager.service.ExchangeRateService;
import com.example.currencymanager.service.provider.FixerApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class ExchangeRateControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @MockitoBean
    private FixerApiService exchangeRateProvider;

    private String baseUrl;

    @BeforeEach
    void setup() {
        openMocks(this);
        baseUrl = "http://localhost:" + port + "/api/rates";
    }

    @Test
    void testGetRatesForCurrency() {
        Map<String, BigDecimal> usdRates = Map.of("EUR", BigDecimal.valueOf(0.85), "GBP", BigDecimal.valueOf(0.75));
        when(exchangeRateProvider.getExchangeRates("USD")).thenReturn(usdRates);
        exchangeRateService.updateRates("USD");

        webTestClient.get()
                .uri(baseUrl + "/USD")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .value(rates -> {
                    assertThat(rates).containsKey("EUR");
                    assertThat(rates).containsKey("GBP");
                    assertThat(rates.get("EUR")).isEqualTo(0.85);
                    assertThat(rates.get("GBP")).isEqualTo(0.75);
                });
    }

    @Test
    void testGetEmptyRatesForCurrency() {
        webTestClient.get()
                .uri(baseUrl + "/JPY")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class);
    }

}
