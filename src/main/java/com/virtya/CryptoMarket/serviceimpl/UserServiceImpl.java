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
    public HashMap<String, String> userGetActualCourse(String secret_key, String currency) {
        Currency btcCur = currencyRepository.findByName("BTC");
        Currency tonCur = currencyRepository.findByName("TON");
        Currency rubCur = currencyRepository.findByName("RUB");

        userRepository.findBySecretKey(secret_key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with secret key = " + secret_key + " does not exist."));


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
    public HashMap<String, String> userExchangeValue(String secret_key, String currencyFrom,
                                                     String currencyTo, String amount) {

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

        Double valueStart = 0.0;
        switch (currencyFrom) {
            case ("RUB") -> valueStart = user.getRUB_balance();
            case ("TON") -> valueStart = user.getTON_balance();
            case ("BTC") -> valueStart = user.getBTC_balance();
        };

        Double myAmount = Double.parseDouble(amount);

        if (valueStart < myAmount) {
            throw new ResourceNotFoundException("You have less money, than you want to get HAHAHAH.");
        }

        Double exchangedValue = 1 / curFrom.getRate() * curTo.getRate();

        switch (currencyFrom) {
            case ("RUB") -> user.setRUB_balance(user.getRUB_balance() - Double.parseDouble(amount));
            case ("TON") -> user.setTON_balance(user.getTON_balance() - Double.parseDouble(amount));
            case ("BTC") -> user.setBTC_balance(user.getBTC_balance() - Double.parseDouble(amount));
        }

        switch (currencyTo) {
            case ("RUB") -> user.setRUB_balance(user.getRUB_balance() + exchangedValue);
            case ("TON") -> user.setTON_balance(user.getTON_balance() + exchangedValue);
            case ("BTC") -> user.setBTC_balance(user.getBTC_balance() + exchangedValue);
        }

        userRepository.save(user);

        Transaction transaction = Transaction.builder().date(new Date()).build();
        transactionRepository.save(transaction);

        HashMap<String, String> outInfo = new HashMap<>();
        outInfo.put("currency from", currencyFrom);
        outInfo.put("currency to", currencyTo);
        outInfo.put("amount from", amount);
        outInfo.put("amount to", exchangedValue.toString());
        return outInfo;
    }
}
