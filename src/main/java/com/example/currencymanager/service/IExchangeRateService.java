package com.example.currencymanager.service;

import java.math.BigDecimal;
import java.util.Map;

public interface IExchangeRateService {

    void updateRates(String baseCurrency);

    Map<String, BigDecimal> getRates(String currencyCode);

}
