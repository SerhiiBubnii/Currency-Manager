package com.example.currencymanager.service.scheduler;

import com.example.currencymanager.model.Currency;
import com.example.currencymanager.repository.CurrencyRepository;
import com.example.currencymanager.service.ExchangeRateService;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class UpdateExchangeRatesScheduledTask {

    private static final Logger logger = getLogger(UpdateExchangeRatesScheduledTask.class);

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateService exchangeRateService;

    public UpdateExchangeRatesScheduledTask(CurrencyRepository currencyRepository,
                                            ExchangeRateService exchangeRateService) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Scheduled(fixedRate = 3600000)
    public void updateExchangeRates() {
        List<Currency> activeCurrencies = currencyRepository.findByActive(true);
        if (activeCurrencies.isEmpty()) {
            logger.info("No active currencies found in the database");
            return;
        }
        for (Currency currency : activeCurrencies) {
            try {
                exchangeRateService.updateRates(currency.getCode());
                logger.info("Exchange rates for " + currency.getCode() + " successfully updated");
            } catch (Exception e) {
                logger.error("Failed to update exchange rates for currency: " + currency.getCode(), e);
            }
        }
        logger.info("Exchange rates updated");
    }

}
