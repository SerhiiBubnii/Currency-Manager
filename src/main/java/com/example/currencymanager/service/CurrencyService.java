package com.example.currencymanager.service;

import com.example.currencymanager.model.Currency;
import com.example.currencymanager.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService implements ICurrencyService {

    public final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public Currency addCurrency(Currency currency) {
        if (currencyRepository.existsByCode(currency.getCode())) {
            throw new RuntimeException("Currency with code " + currency.getCode() + " already exists");
        }
        return currencyRepository.save(currency);
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Currency with code " + code + " not found"));
    }

}
