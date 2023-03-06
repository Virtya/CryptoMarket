package com.virtya.CryptoMarket.serviceimpl;

import com.virtya.CryptoMarket.entity.Currency;
import com.virtya.CryptoMarket.entity.Transaction;
import com.virtya.CryptoMarket.exception.ResourceNotFoundException;
import com.virtya.CryptoMarket.repository.AdminRepository;
import com.virtya.CryptoMarket.repository.CurrencyRepository;
import com.virtya.CryptoMarket.repository.TransactionRepository;
import com.virtya.CryptoMarket.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CurrencyRepository currencyRepository;
    private final AdminRepository adminRepository;
    private final TransactionRepository transactionRepository;
    final List<String> currencies = Arrays.asList("RUB", "TON", "BTC");

    @Override
    public HashMap<String, String> adminGetActualCourse(String secret_key, String currency) {

        adminRepository.findBySecretKey(secret_key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin with secret key = " + secret_key + " does not exist."));
        if (currencyRepository.findByName(currency) == null) {
                throw new ResourceNotFoundException(
                    "The currency with name = " + currency + " does not exist.");
        }

        Currency btcCur = currencyRepository.findByName("BTC");
        Currency tonCur = currencyRepository.findByName("TON");
        Currency rubCur = currencyRepository.findByName("RUB");


        HashMap<String, String> actualCur = new HashMap<>();

        switch (currency) {
            case ("BTC") -> {
                actualCur.put("TON", String.valueOf(1 / btcCur.getRate() * tonCur.getRate()));
                actualCur.put("RUB",  String.valueOf(1 / btcCur.getRate() * rubCur.getRate()));
            }
            case ("TON") -> {
                actualCur.put("BTC",  String.valueOf(1 / tonCur.getRate() * btcCur.getRate()));
                actualCur.put("RUB",  String.valueOf(1 / tonCur.getRate() * rubCur.getRate()));
            }
            case ("RUB") -> {
                actualCur.put("BTC",  String.valueOf(1 / rubCur.getRate() * btcCur.getRate()));
                actualCur.put("TON",  String.valueOf(1 / rubCur.getRate() * tonCur.getRate()));
            }
        }

        Transaction transaction = Transaction.builder().date(new Date()).build();
        transactionRepository.save(transaction);

        return actualCur;
    }

    @Override
    public HashMap<String, String> adminChangeActualCourse(String secret_key, String currency, List<String> names,
                                                           List<String> values) {
        int i = 0;
        HashMap<String, String> actualRate = new HashMap<>();

        adminRepository.findBySecretKey(secret_key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin with secret key = " + secret_key + " does not exist."));

        while (i < 2) {
            Currency cur = currencyRepository.findByName(names.get(i));
                    if (cur == null) {
                        throw new ResourceNotFoundException("The currency does not exist.");
                    }
            cur.setRate(Double.parseDouble(values.get(i)));
            currencyRepository.save(cur);

            actualRate.put(cur.getName(), cur.getRate().toString());
            i++;
        }

        Transaction transaction = Transaction.builder().date(new Date()).build();
        transactionRepository.save(transaction);

        return actualRate;
    }

    @Override
    public String adminGetSumForVal(String secret_key, String cur) {
        adminRepository.findBySecretKey(secret_key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin with secret key = " + secret_key + " does not exist."));

        return adminRepository.getSumFromAllUsers(cur).toString();
    }

    @Override
    public String adminGetCountTransactions(String secret_key, String dateFrom, String dateTo) throws ParseException {
        DateFormat format = new SimpleDateFormat("d.M.yyyy", Locale.ENGLISH);
        Date dateFr = format.parse(dateFrom);
        Date dateT = format.parse(dateTo);

        return Integer.toString(adminRepository.countAmountOfTransact(dateFr, dateT));
    }
}
