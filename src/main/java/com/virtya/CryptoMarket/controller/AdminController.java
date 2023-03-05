package com.virtya.CryptoMarket.controller;


import com.virtya.CryptoMarket.dto.ChangeRateAdminDto;
import com.virtya.CryptoMarket.dto.ChangedRateAdminDto;
import com.virtya.CryptoMarket.dto.forwatchrate.AdminCurrencyDto;
import com.virtya.CryptoMarket.dto.error.ErrorDto;
import com.virtya.CryptoMarket.dto.forexchangemoney.AlreadyExchangedValueDto;
import com.virtya.CryptoMarket.dto.forexchangemoney.ExchangeValueDto;
import com.virtya.CryptoMarket.dto.forwatchrate.GetCurrencyDto;
import com.virtya.CryptoMarket.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin", produces =  "application/json")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/rate")
    public Object getWatchRateUser(@RequestBody AdminCurrencyDto myAdmin) {
        GetCurrencyDto cur = new GetCurrencyDto();
        HashMap<String, String> hashMapRate = adminService.adminGetActualCourse(myAdmin.getSecretKey(), myAdmin.getCurrency());

        switch (myAdmin.getCurrency()) {
            case ("RUB") -> {
                cur.setRUB_wallet("1");
                cur.setTON_wallet(hashMapRate.get("TON"));
                cur.setBTC_wallet(hashMapRate.get("BTC"));
                return cur;
            }
            case ("TON") -> {
                cur.setTON_wallet("1");
                cur.setRUB_wallet(hashMapRate.get("RUB"));
                cur.setBTC_wallet(hashMapRate.get("BTC"));
                return cur;
            }
            case ("BTC") -> {
                cur.setBTC_wallet("1");
                cur.setRUB_wallet(hashMapRate.get("RUB"));
                cur.setTON_wallet(hashMapRate.get("TON"));
                return cur;
            }
        }

        Date date = new Date();
        return new ErrorDto("This type of currency does not exist.", date);
    }

    @PostMapping("/change")
    public Object postChangeRate(@RequestBody ChangeRateAdminDto myAdmin) {

        List<String> currencies = new ArrayList<>();
        List<String> currenciesValue = new ArrayList<>();

        HashMap<String, String> currenciesRes;
        ChangedRateAdminDto changedRate = new ChangedRateAdminDto();

        switch (myAdmin.getBase_currency()) {
            case ("RUB") -> {
                currencies.add("TON");
                currencies.add("BTC");
                currenciesValue.add(myAdmin.getTON());
                currenciesValue.add(myAdmin.getBTC());
                currenciesRes = adminService.adminChangeActualCourse(myAdmin.getSecretKey(),
                        myAdmin.getBase_currency(), currencies, currenciesValue);
                changedRate.setTON(currenciesRes.get("TON"));
                changedRate.setBTC(currenciesRes.get("BTC"));
                changedRate.setRUB("not changed");
                return changedRate;
            }
            case ("TON") -> {
                currencies.add("RUB");
                currencies.add("BTC");
                currenciesValue.add(myAdmin.getRUB());
                currenciesValue.add(myAdmin.getBTC());
                currenciesRes = adminService.adminChangeActualCourse(myAdmin.getSecretKey(),
                        myAdmin.getBase_currency(), currencies, currenciesValue);
                changedRate.setRUB(currenciesRes.get("RUB"));
                changedRate.setBTC(currenciesRes.get("BTC"));
                changedRate.setTON("not changed");
                return changedRate;
            }
            case ("BTC") -> {
                currencies.add("RUB");
                currencies.add("TON");
                currenciesValue.add(myAdmin.getRUB());
                currenciesValue.add(myAdmin.getTON());
                currenciesRes = adminService.adminChangeActualCourse(myAdmin.getSecretKey(),
                        myAdmin.getBase_currency(), currencies, currenciesValue);
                changedRate.setRUB(currenciesRes.get("RUB"));
                changedRate.setTON(currenciesRes.get("TON"));
                changedRate.setBTC("not changed");
                return changedRate;
            }
        }

        Date date = new Date();
        return new ErrorDto("This type of currency does not exist.", date);
    }

}
