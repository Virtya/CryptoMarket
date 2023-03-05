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
    public HashMap<String, Double> adminGetActualCourse(String secret_key, String currency) {
        /*Currency tonCur =  Currency.builder().name("TON").rate(0.005556).build();
        Currency rubCur =  Currency.builder().name("RUB").rate((double) 1).build();
        Currency btcCur = Currency.builder().name("BTC").rate(0.000095).build();

        currencyRepository.save(btcCur);
        currencyRepository.save(tonCur);
        currencyRepository.save(rubCur);*/

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


        HashMap<String, Double> actualCur = new HashMap<>();
        switch (currency) {
            case ("BTC") -> {
                actualCur.put("TON", btcCur.getRate() / tonCur.getRate());
                actualCur.put("RUB", btcCur.getRate() / rubCur.getRate());
            }
            case ("TON") -> {
                actualCur.put("BTC", tonCur.getRate() / btcCur.getRate());
                actualCur.put("RUB", tonCur.getRate() / rubCur.getRate());
            }
            case ("RUB") -> {
                actualCur.put("BTC", rubCur.getRate() / btcCur.getRate());
                actualCur.put("TON", rubCur.getRate() / tonCur.getRate());
            }
        }

        Transaction transaction = Transaction.builder().date(new Date()).build();
        transactionRepository.save(transaction);

        return actualCur;
    }

    @Override
    public HashMap<String, Double> adminChangeActualCourse(String secret_key, String currency, List<String> names,
                                                           List<Double> values) {
        int i = 0;
        HashMap<String, Double> actualRate = new HashMap<>();

        adminRepository.findBySecretKey(secret_key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin with secret key = " + secret_key + " does not exist."));
        if (currencyRepository.findByName(currency) == null) {
                throw new ResourceNotFoundException(
                    "The currency with name = " + currency + " does not exist.");
        }
        while (names.listIterator().hasNext()) {
            Currency cur = currencyRepository.findByName(names.get(i));
                    if (cur == null) {
                        throw new ResourceNotFoundException("The currency does not exist.");
                    }
            cur.setRate(values.get(i));
            currencyRepository.save(cur);

            actualRate.put(cur.getName(), cur.getRate());
            i++;
        }

        Transaction transaction = Transaction.builder().date(new Date()).build();
        transactionRepository.save(transaction);

        return actualRate;
    }

    @Override
    public Double adminGetSumForVal(String secret_key, String cur) {
        adminRepository.findBySecretKey(secret_key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin with secret key = " + secret_key + " does not exist."));
        if (!currencies.contains(cur)) {
            throw new ResourceNotFoundException("The currency does not exist.");
        }
        return adminRepository.getSumFromAllUsers(cur);
    }

    @Override
    public int adminGetCountTransactions(String secret_key, String dateFrom, String dateTo) throws ParseException {
        DateFormat format = new SimpleDateFormat("d.M.yyyy", Locale.ENGLISH);
        Date dateFr = format.parse(dateFrom);
        Date dateT = format.parse(dateTo);

        return adminRepository.countAmountOfTransact(dateFr, dateT);
    }
}
