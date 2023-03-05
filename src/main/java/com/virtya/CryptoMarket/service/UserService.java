package com.virtya.CryptoMarket.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserService {
    String userRegistrate(String username, String email);
    ArrayList<Double> userWatchBalance(String secret_key);
    Double userReplenishmentWallet(String secret_key, String type, String money);
    Double userGetMoneyFromMarket(String secret_key, String currency, String money, String cardOrWallet);
    HashMap<String, String> userGetActualCourse(String secret_key, String currency);
    HashMap<String, String> userExchangeValue(String secret_key, String currencyFrom, String currencyTo, String amount);
}
