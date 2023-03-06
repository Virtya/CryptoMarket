package com.virtya.CryptoMarket.controller;


import com.virtya.CryptoMarket.dto.forchangerate.ChangeRateAdminDto;
import com.virtya.CryptoMarket.dto.forchangerate.ChangedRateAdminDto;
import com.virtya.CryptoMarket.dto.forgetusersbalance.AdminUsersBalanceDto;
import com.virtya.CryptoMarket.dto.forgetusersbalance.UsersBalanceSumBtcDto;
import com.virtya.CryptoMarket.dto.forgetusersbalance.UsersBalanceSumRubDto;
import com.virtya.CryptoMarket.dto.forgetusersbalance.UsersBalanceSumTonDto;
import com.virtya.CryptoMarket.dto.fortransaction.AdminCheckTransactionsDto;
import com.virtya.CryptoMarket.dto.fortransaction.TransactionCountDto;
import com.virtya.CryptoMarket.dto.forwatchrate.AdminCurrencyDto;
import com.virtya.CryptoMarket.dto.error.ErrorDto;
import com.virtya.CryptoMarket.dto.forwatchrate.GetCurrencyDto;
import com.virtya.CryptoMarket.exception.ResourceNotFoundException;
import com.virtya.CryptoMarket.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin", produces =  "application/json")
public class AdminController {

    private final AdminService adminService;

    // Для просмотра курса валют админом
    @GetMapping("/rate")
    public Object getWatchRateAdmin(@RequestBody AdminCurrencyDto myAdmin) {
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

        Date date = new Date(System.currentTimeMillis());
        return new ErrorDto("This type of currency does not exist.", date);
    }

    // Для смены курса валют админом
    @PostMapping("/change")
    public Object postChangeRate(@RequestBody ChangeRateAdminDto myAdmin) {

        List<String> currencies = new ArrayList<>();
        List<String> currenciesValue = new ArrayList<>();

        HashMap<String, String> currenciesRes;
        ChangedRateAdminDto changedRate = new ChangedRateAdminDto();

        switch (myAdmin.getBase_currency()) {
            case ("RUB") -> {
                adminService.addToCurrency(currencies, "TON","BTC");
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
                adminService.addToCurrency(currencies, "RUB","BTC");
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
                adminService.addToCurrency(currencies, "RUB","TON");
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

        Date date = new Date(System.currentTimeMillis());
        return new ErrorDto("This type of currency does not exist.", date);
    }

    // Подсчет баланса на кошельках пользователей
    @GetMapping("/balance")
    public Object getUsersBalance(@RequestBody AdminUsersBalanceDto myAdmin) {

        String sumUser = adminService.adminGetSumForVal(myAdmin.getSecretKey(), myAdmin.getCurrency());

        switch (myAdmin.getCurrency()) {
            case ("RUB") -> {
                UsersBalanceSumRubDto cur = new UsersBalanceSumRubDto();
                cur.setRUB(sumUser);
                return cur;
            }
            case ("TON") -> {
                UsersBalanceSumTonDto cur = new UsersBalanceSumTonDto();
                cur.setTON(sumUser);
                return cur;
            }
            case ("BTC") -> {
                UsersBalanceSumBtcDto cur = new UsersBalanceSumBtcDto();
                cur.setBTC(sumUser);
                return cur;
            }
        }

        Date date = new Date(System.currentTimeMillis());
        return new ErrorDto("This type of currency does not exist.", date);
    }

    // Для подсчета количества транзакций за определенный срок
    @GetMapping("/transaction")
    public Object getTransactionsCount(@RequestBody AdminCheckTransactionsDto myAdmin) {
        try {
            String transactionCount = adminService.adminGetCountTransactions(myAdmin.getSecretKey(), myAdmin.getDate_from(), myAdmin.getDate_to());
            TransactionCountDto trCnt = new TransactionCountDto();

            trCnt.setTransaction_count(transactionCount);

            return trCnt;
        } catch (ParseException e) {
            Date date = new Date(System.currentTimeMillis());
            return new ErrorDto(e.getMessage(), date);
        }
    }

}
