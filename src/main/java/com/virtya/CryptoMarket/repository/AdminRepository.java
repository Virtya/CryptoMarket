package com.virtya.CryptoMarket.repository;

import com.virtya.CryptoMarket.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsBySecretKey(String secretKey);

    Optional<Admin> findBySecretKey(String secretKey);

    @Query("select case when ?1 = 'RUB' then sum(u.RUB_balance) " +
            "when ?1 = 'TON' then sum(u.TON_balance) else sum(u.BTC_balance) end from OurUser u")
    Double getSumFromAllUsers(String cur);

    @Query("select count(t) from Transaction t where t.date between ?1 and ?2")
    int countAmountOfTransact(Date dateFrom, Date dateTo);
}
