package com.example.currencymanager.service;

import com.example.currencymanager.model.Currency;

import java.util.List;

public interface ICurrencyService {

    List<Currency> getAllCurrencies();

    Currency addCurrency(Currency currency);

    Currency getCurrencyByCode(String code);

}
