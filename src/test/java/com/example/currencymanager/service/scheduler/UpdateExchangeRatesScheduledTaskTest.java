package com.example.currencymanager.service.scheduler;

import com.example.currencymanager.model.Currency;
import com.example.currencymanager.repository.CurrencyRepository;
import com.example.currencymanager.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

public class UpdateExchangeRatesScheduledTaskTest {

    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private ExchangeRateService exchangeRateService;
    @InjectMocks
    private UpdateExchangeRatesScheduledTask scheduledTask;

    private Currency currency;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currency = new Currency();
        currency.setCode("USD");
        currency.setActive(true);
    }

    @Test
    void testUpdateExchangeRatesWhenActiveCurrenciesExist() {
        when(currencyRepository.findByActive(true)).thenReturn(List.of(currency));
        scheduledTask.updateExchangeRates();
        verify(exchangeRateService, times(1)).updateRates("USD");
    }

    @Test
    void testUpdateExchangeRatesWhenNoActiveCurrencies() {
        when(currencyRepository.findByActive(true)).thenReturn(List.of());
        scheduledTask.updateExchangeRates();
        verify(exchangeRateService, never()).updateRates(anyString());
    }

    @Test
    void testUpdateExchangeRatesWithException() {
        when(currencyRepository.findByActive(true)).thenReturn(List.of(currency));
        doThrow(new RuntimeException("API call failed")).when(exchangeRateService).updateRates("USD");
        scheduledTask.updateExchangeRates();
        verify(exchangeRateService, times(1)).updateRates("USD");
    }

}
