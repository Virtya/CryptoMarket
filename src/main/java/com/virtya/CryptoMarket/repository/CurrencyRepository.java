package com.virtya.CryptoMarket.repository;

import com.virtya.CryptoMarket.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    boolean existsByName(String name);
    Currency findByName(String name);
}
