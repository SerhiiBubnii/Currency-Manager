package com.example.currencymanager.service;

import com.example.currencymanager.model.Currency;
import com.example.currencymanager.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    private Currency currency;

    @BeforeEach
    void setUp() {
        openMocks(this);
        currency = new Currency();
        currency.setCode("USD");
        currency.setName("United States Dollar");
    }

    @Test
    void testGetAllCurrencies() {
        when(currencyRepository.findAll()).thenReturn(List.of(currency));
        var currencies = currencyService.getAllCurrencies();
        assertNotNull(currencies);
        assertEquals(1, currencies.size());
        assertEquals("USD", currencies.get(0).getCode());
        verify(currencyRepository, times(1)).findAll();
    }

    @Test
    void testAddCurrency() {
        when(currencyRepository.existsByCode("USD")).thenReturn(false);
        when(currencyRepository.save(currency)).thenReturn(currency);
        var savedCurrency = currencyService.addCurrency(currency);
        assertNotNull(savedCurrency);
        assertEquals("USD", savedCurrency.getCode());
        verify(currencyRepository, times(1)).existsByCode("USD");
        verify(currencyRepository, times(1)).save(currency);
    }

    @Test
    void testAddCurrencyWhenAlreadyExists() {
        when(currencyRepository.existsByCode("USD")).thenReturn(true);
        var exception = assertThrows(RuntimeException.class, () -> currencyService.addCurrency(currency));
        assertEquals("Currency with code USD already exists", exception.getMessage());
        verify(currencyRepository, times(1)).existsByCode("USD");
        verify(currencyRepository, never()).save(currency);
    }

    @Test
    void testGetCurrencyByCode() {
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(currency));
        var result = currencyService.getCurrencyByCode("USD");
        assertNotNull(result);
        assertEquals("USD", result.getCode());
        verify(currencyRepository, times(1)).findByCode("USD");
    }

    @Test
    void testGetCurrencyByCodeWhenNotFound() {
        when(currencyRepository.findByCode("EUR")).thenReturn(Optional.empty());
        var exception = assertThrows(RuntimeException.class, () -> currencyService.getCurrencyByCode("EUR"));
        assertEquals("Currency with code EUR not found", exception.getMessage());
        verify(currencyRepository, times(1)).findByCode("EUR");
    }

}
