package com.example.currencymanager.service;

import com.example.currencymanager.model.ExchangeRate;
import com.example.currencymanager.repository.ExchangeRateRepository;
import com.example.currencymanager.service.provider.ExchangeRateProvider;
import com.example.currencymanager.service.provider.ExchangeRateProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateProviderFactory providerFactory;
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @Mock
    private ExchangeRateProvider provider;
    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private Map<String, BigDecimal> exchangeRates;

    @BeforeEach
    void setUp() {
        openMocks(this);
        exchangeRates = Map.of("USD", new BigDecimal("1.2"), "EUR", new BigDecimal("0.85"));
        when(providerFactory.getProvider()).thenReturn(provider);
    }

    @Test
    void testUpdateRates() {
        when(provider.getExchangeRates("USD")).thenReturn(exchangeRates);
        exchangeRateService.updateRates("USD");
        verify(exchangeRateRepository, times(1)).save(any(ExchangeRate.class));
        assertEquals(exchangeRates, exchangeRateService.getRates("USD"));
    }

    @Test
    void testUpdateRatesWhenNoRates() {
        when(provider.getExchangeRates("USD")).thenReturn(Map.of());
        exchangeRateService.updateRates("USD");
        verify(exchangeRateRepository, never()).save(any(ExchangeRate.class));
        assertThrows(RuntimeException.class, () -> exchangeRateService.getRates("USD"));
    }

    @Test
    void testGetRatesWhenRatesAreCached() {
        when(provider.getExchangeRates("USD")).thenReturn(exchangeRates);
        exchangeRateService.updateRates("USD");
        Map<String, BigDecimal> cachedRates = exchangeRateService.getRates("USD");
        assertEquals(exchangeRates, cachedRates);
    }

    @Test
    void testGetRatesWhenRatesNotFound() {
        var exception = assertThrows(RuntimeException.class, () -> exchangeRateService.getRates("EUR"));
        assertEquals("Exchange rates not found for code: EUR", exception.getMessage());
    }

}
