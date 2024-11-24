package com.example.currencymanager.service.provider;

import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeRateProvider {

    Map<String, BigDecimal> getExchangeRates(String baseCurrency);

}
