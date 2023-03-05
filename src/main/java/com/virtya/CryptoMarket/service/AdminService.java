package com.virtya.CryptoMarket.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public interface AdminService {
    HashMap<String, String> adminGetActualCourse(String secret_key, String currency);
    HashMap<String, String> adminChangeActualCourse(String secret_key, String currency, List<String> names,
                                                    List<String> values);
    String adminGetSumForVal(String secret_key, String cur);
    String adminGetCountTransactions(String secret_key, String dateFrom, String dateTo) throws ParseException;
}
