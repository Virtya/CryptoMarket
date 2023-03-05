package com.virtya.CryptoMarket.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public interface AdminService {
    HashMap<String, Double> adminGetActualCourse(String secret_key, String currency);
    HashMap<String, Double> adminChangeActualCourse(String secret_key, String currency, List<String> names,
                                                    List<Double> values);
    Double adminGetSumForVal(String secret_key, String cur);
    int adminGetCountTransactions(String secret_key, String dateFrom, String dateTo) throws ParseException;
}
