package com.virtya.CryptoMarket.serviceimpl;

import com.virtya.CryptoMarket.entity.Currency;
import com.virtya.CryptoMarket.entity.Transaction;
import com.virtya.CryptoMarket.entity.OurUser;
import com.virtya.CryptoMarket.exception.ResourceNotFoundException;
import com.virtya.CryptoMarket.exception.UnexpectedTypeException;
import com.virtya.CryptoMarket.repository.CurrencyRepository;
import com.virtya.CryptoMarket.repository.TransactionRepository;
import com.virtya.CryptoMarket.repository.UserRepository;
import com.virtya.CryptoMarket.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final TransactionRepository transactionRepository;
    final List<String> currencies = Arrays.asList("RUB", "TON", "BTC");

    @Override
    @Transactional
    public String userRegistrate(String username, String email) {
        String myHash = DigestUtils.md5Hex(username + email);

        OurUser user = OurUser.builder().username(username).email(email)
                .secretKey(myHash).RUB_balance((double) 0).TON_balance((double) 0).BTC_balance((double) 0).build();


        Transaction transaction = Transaction.builder().date(new Date()).build();
        transactionRepository.save(transaction);

        userRepository.save(user);
        return myHash;
    }

    @Override
    public ArrayList<Double> userWatchBalance(String secret_key) {
        OurUser user = userRepository.findBySecretKey(secret_key)
                .orElseThrow(()->new ResourceNotFoundException(
                        "The user with secret key = "+ secret_key +" does not exist."));

        Transaction transaction = Transaction.builder().date(new Date()).build();
        transactionRepository.save(transaction);

        ArrayList<Double> usersBalances = new ArrayList<>();
        usersBalances.add(user.getRUB_balance());
        usersBalances.add(user.getTON_balance());
        usersBalances.add(user.getBTC_balance());

        return usersBalances;
    }

    @Override
    public Double userReplenishmentWallet(String secret_key, String type, String money) {
        double value = 0.0;

            Double myMoney = Double.parseDouble(money);

        OurUser user = userRepository.findBySecretKey(secret_key)
                .orElseThrow(()->new ResourceNotFoundException(
                        "The user with secret key = "+ secret_key +" does not exist."));
        if (!currencies.contains(type)) {
            throw new ResourceNotFoundException("Wallet with type = " + type + " does not exist.");
        }

        switch (type) {
            case ("RUB") -> {
                value = user.getRUB_balance() + myMoney;
                user.setRUB_balance(value);
            }
            case ("TON") -> {
                value = user.getTON_balance() + myMoney;
                user.setTON_balance(value);
            }
            default -> {
                value = user.getBTC_balance() + myMoney;
                user.setBTC_balance(value);
            }
        }

        Transaction transaction = Transaction.builder().date(new Date()).build();
        transactionRepository.save(transaction);
        userRepository.save(user);

        return value;
    }

    @Override
    public Double userGetMoneyFromMarket(String secret_key, String currency, String money, String cardOrWallet) {

        OurUser user = userRepository.findBySecretKey(secret_key)
                .orElseThrow(()->new ResourceNotFoundException(
                        "The user with secret key = "+ secret_key +" does not exist."));
        if (!currencies.contains(currency)) {
            throw new ResourceNotFoundException("Currency " + currency + "does not exist");
        }
        if (Objects.equals(currency, "RUB") && cardOrWallet.length() != 19) {
            throw new UnexpectedTypeException("You wrote not a credit card.");
        }
        if ((Objects.equals(currency, "TON") || Objects.equals(currency, "BTC"))
                && cardOrWallet.contains(" ")) {
            throw new UnexpectedTypeException("You wrote not a wallet.");
        }

        Double value = 0.0;
        switch (currency) {
            case ("RUB") -> value = user.getRUB_balance();
            case ("TON") -> value = user.getTON_balance();
            case ("BTC") -> value = user.getBTC_balance();
        }

        Double myMoney = Double.parseDouble(money);
        if (value < myMoney) {
            throw new ResourceNotFoundException("You have less money, than you want to get HAHAHAH.");
        }

        value -= myMoney;
        switch (currency) {
            case ("RUB") -> user.setRUB_balance(value);
            case ("TON") -> user.setTON_balance(value);
            case ("BTC") -> user.setBTC_balance(value);
        }

        Transaction transaction = Transaction.builder().date(new Date()).build();
        transactionRepository.save(transaction);

        userRepository.save(user);
        return value;
    }

    @Override
    public HashMap<String, Double> userGetActualCourse(String secret_key, String currency) {
        Currency btcCur = currencyRepository.findByName("BTC");
        Currency tonCur = currencyRepository.findByName("TON");
        Currency rubCur = currencyRepository.findByName("RUB");

        userRepository.findBySecretKey(secret_key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with secret key = " + secret_key + " does not exist."));


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

    private double getValueFromCur(OurUser user, String cur) {
        Double value = 0.0;
        switch (cur) {
            case ("RUB") -> value = user.getRUB_balance();
            case ("TON") -> value = user.getTON_balance();
            case ("BTC") -> value = user.getBTC_balance();
        }
        return value;
    }

    private void setValueFromCur(OurUser user, String cur, double value) {
        switch (cur) {
            case ("RUB") -> user.setRUB_balance(value);
            case ("TON") -> user.setTON_balance(value);
            case ("BTC") -> user.setBTC_balance(value);
        }
    }

    @Override
    public HashMap<String, String> userExchangeValue(String secret_key, String currencyFrom,
                                                     String currencyTo, Double amount) {
        OurUser user = userRepository.findBySecretKey(secret_key)
                .orElseThrow(()->new ResourceNotFoundException(
                        "The user with secret key = "+ secret_key +" does not exist."));
        Currency curFrom = currencyRepository.findByName(currencyFrom);
               if (curFrom == null) {
                   throw new ResourceNotFoundException(
                        "The currency with name = "+ currencyFrom +" does not exist.");
               }
        Currency curTo = currencyRepository.findByName(currencyTo);
        if (curTo == null) {
            throw new ResourceNotFoundException(
                    "The currency with name = "+ currencyTo +" does not exist.");
        }
        double valueStart = getValueFromCur(user, currencyFrom);
        double valueEnd = getValueFromCur(user, currencyTo);


        if (valueStart < amount) {
            throw new ResourceNotFoundException("You have less money, than you want to get HAHAHAH.");
        }

        Double exchangedValue = curFrom.getRate() * curTo.getRate();
        valueStart -= amount;
        valueEnd += amount / exchangedValue;

        setValueFromCur(user, currencyFrom, valueStart);
        setValueFromCur(user, currencyTo, valueEnd);

        userRepository.save(user);

        Transaction transaction = Transaction.builder().date(new Date()).build();
        transactionRepository.save(transaction);

        HashMap<String, String> outInfo = new HashMap<>();
        outInfo.put("currency from: ", currencyFrom);
        outInfo.put("currency to: ", currencyTo);
        outInfo.put("amount from: ", amount.toString());
        outInfo.put("amount to: ", exchangedValue.toString());
        return outInfo;
    }
}
