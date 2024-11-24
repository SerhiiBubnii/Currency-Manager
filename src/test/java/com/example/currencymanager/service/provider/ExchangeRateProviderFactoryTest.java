package com.example.currencymanager.service.provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class ExchangeRateProviderFactoryTest {

    @Mock
    private FixerApiService fixerApiService;
    @Mock
    private ExchangeRatesApiService exchangeRatesApiService;
    @Mock
    private CurrencyLayerApiService currencyLayerApiService;
    @InjectMocks
    private ExchangeRateProviderFactory factory;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testGetProviderFixer() {
        ReflectionTestUtils.setField(factory, "provider", "fixer");
        var provider = factory.getProvider();
        assertEquals(fixerApiService, provider, "Expected FixerApiService as provider");
    }

    @Test
    void testGetProviderExchangeRatesApi() {
        ReflectionTestUtils.setField(factory, "provider", "exchangeratesapi");
        var provider = factory.getProvider();
        assertEquals(exchangeRatesApiService, provider, "Expected ExchangeRatesApiService as provider");
    }

    @Test
    void testGetProviderCurrencyLayer() {
        ReflectionTestUtils.setField(factory, "provider", "currencylayer");
        var provider = factory.getProvider();
        assertEquals(currencyLayerApiService, provider, "Expected CurrencyLayerApiService as provider");
    }

    @Test
    void testGetProviderUnsupported() {
        ReflectionTestUtils.setField(factory, "provider", "unsupported");
        var exception = assertThrows(IllegalArgumentException.class, factory::getProvider);
        assertEquals("Unsupported provider: unsupported", exception.getMessage());
    }
    
}
