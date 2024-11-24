package com.example.currencymanager.controller;

import com.example.currencymanager.model.Currency;
import com.example.currencymanager.service.ICurrencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final ICurrencyService currencyService;

    public CurrencyController(ICurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public List<Currency> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @PostMapping
    public Currency addCurrency(@RequestBody Currency currency) {
        return currencyService.addCurrency(currency);
    }

    @GetMapping("/{currencyCode}")
    public Currency getCurrency(@PathVariable String currencyCode) {
        return currencyService.getCurrencyByCode(currencyCode);
    }

}
