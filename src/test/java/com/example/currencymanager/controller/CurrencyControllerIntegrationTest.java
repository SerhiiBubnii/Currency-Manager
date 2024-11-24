package com.example.currencymanager.controller;

import com.example.currencymanager.model.Currency;
import com.example.currencymanager.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class CurrencyControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private WebTestClient webTestClient;

    private String baseUrl;

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/currencies";
        currencyRepository.deleteAll();
    }

    @Test
    void testGetAllCurrencies() {
        currencyRepository.save(createCurrency("USD", "US Dollar", true));
        currencyRepository.save(createCurrency("EUR", "Euro", false));

        webTestClient.get()
                .uri(baseUrl)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Currency.class)
                .value(currencies -> {
                    assertThat(currencies).hasSize(2);
                    assertThat(currencies).extracting(Currency::getCode).containsExactlyInAnyOrder("USD", "EUR");
                });
    }

    @Test
    void testAddCurrency() {
        Currency newCurrency = createCurrency("GBP", "British Pound", true);
        webTestClient.post()
                .uri(baseUrl)
                .contentType(APPLICATION_JSON)
                .bodyValue(newCurrency)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Currency.class)
                .value(savedCurrency -> {
                    assertThat(savedCurrency.getId()).isNotNull();
                    assertThat(savedCurrency.getCode()).isEqualTo("GBP");
                });

        List<Currency> currencies = currencyRepository.findAll();
        assertThat(currencies).hasSize(1);
        assertThat(currencies.get(0).getCode()).isEqualTo("GBP");
    }

    @Test
    void testAddDuplicateCurrency() {
        Currency usd = createCurrency("USD", "US Dollar", true);
        currencyRepository.save(usd);
        Currency duplicateCurrency = createCurrency("USD", "US Dollar", true);

        webTestClient.post()
                .uri(baseUrl)
                .contentType(APPLICATION_JSON)
                .bodyValue(duplicateCurrency)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testGetCurrency() {
        Currency usd = createCurrency("USD", "US Dollar", true);
        currencyRepository.save(usd);

        webTestClient.get()
                .uri(baseUrl + "/USD")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Currency.class)
                .value(currency -> {
                    assertThat(currency.getId()).isNotNull();
                    assertThat(currency.getCode()).isEqualTo("USD");
                    assertThat(currency.getName()).isEqualTo("US Dollar");
                });
    }

    @Test
    void testGetNotExistCurrency() {
        webTestClient.get()
                .uri(baseUrl + "/XYZ")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    private static Currency createCurrency(String code, String name, boolean active) {
        var currency = new Currency();
        currency.setId(null);
        currency.setCode(code);
        currency.setName(name);
        currency.setActive(active);
        return currency;
    }

}
