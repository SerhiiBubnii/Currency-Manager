package com.example.currencymanager.service.provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class FixerApiServiceTest {

    @Mock
    private WebClient webClientMock;
    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;
    @InjectMocks
    private FixerApiService fixerApiService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        ReflectionTestUtils.setField(fixerApiService, "apiKey", "test-api-key");
    }

    @Test
    void testGetExchangeRatesSuccess() {
        String baseCurrency = "USD";
        Map<String, BigDecimal> rates = Map.of(
                "USDAUD", BigDecimal.valueOf(1.5),
                "USDEUR", BigDecimal.valueOf(0.85)
        );
        var mockResponse = new FixerApiService.FixerResponse();
        mockResponse.setRates(rates);
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(FixerApiService.FixerResponse.class)).thenReturn(Mono.just(mockResponse));
        Map<String, BigDecimal> exchangeRates = fixerApiService.getExchangeRates(baseCurrency);
        assertEquals(rates, exchangeRates, "Exchange rates should match the mocked response");
    }

    @Test
    void testGetExchangeRatesError() {
        String baseCurrency = "USD";
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenThrow(WebClientResponseException.class);
        assertThrows(WebClientResponseException.class, () -> fixerApiService.getExchangeRates(baseCurrency));
    }

}
