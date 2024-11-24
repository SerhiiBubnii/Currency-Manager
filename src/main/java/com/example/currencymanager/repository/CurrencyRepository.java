package com.example.currencymanager.repository;

import com.example.currencymanager.model.Currency;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends ListCrudRepository<Currency, Long> {

    Optional<Currency> findByCode(String code);

    boolean existsByCode(String code);

    List<Currency> findByActive(boolean active);

}
