package com.virtya.CryptoMarket.controller;


import com.virtya.CryptoMarket.dto.error.ErrorDto;
import com.virtya.CryptoMarket.dto.forexchangemoney.AlreadyExchangedValueDto;
import com.virtya.CryptoMarket.dto.forexchangemoney.ExchangeValueDto;
import com.virtya.CryptoMarket.dto.forgetmoney.UserGetMoneyMarketDto;
import com.virtya.CryptoMarket.dto.forregister.UserDto;
import com.virtya.CryptoMarket.dto.forregister.UserRegistrateDto;
import com.virtya.CryptoMarket.dto.forreplenishwallet.UserReplenishWalletBtcDto;
import com.virtya.CryptoMarket.dto.forreplenishwallet.UserReplenishWalletDto;
import com.virtya.CryptoMarket.dto.forreplenishwallet.UserReplenishWalletRubDto;
import com.virtya.CryptoMarket.dto.forreplenishwallet.UserReplenishWalletTonDto;
import com.virtya.CryptoMarket.dto.forwatchbalance.WalletDto;
import com.virtya.CryptoMarket.dto.forwatchrate.GetCurrencyDto;
import com.virtya.CryptoMarket.dto.forwatchrate.UserCurrencyDto;
import com.virtya.CryptoMarket.service.UserService;
import lombok.AllArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/user", produces =  "application/json")
public class UserController {

    private final UserService userService;

    @PostMapping("/registrate")
    public Object postRegistrationUser(@RequestBody UserRegistrateDto myUser) {

        UserDto usr = new UserDto();

        usr.setSecretKey(userService.userRegistrate(myUser.getUsername(), myUser.getEmail()));

        return usr;
    }

    @GetMapping("/watch")
    public Object getWatchBalanceUser(@RequestBody UserDto myUser) {
        List<Double> userBalance;

        userBalance = userService.userWatchBalance(myUser.getSecretKey());

        WalletDto wallet = new WalletDto();
        wallet.setRUB_wallet(userBalance.get(0).toString());
        wallet.setTON_wallet(userBalance.get(1).toString());
        wallet.setBTC_wallet(userBalance.get(2).toString());

        return wallet;
    }

    @PostMapping("/replenish")
    public Object postReplenishWallet(@RequestBody UserReplenishWalletDto myUser) {
        String type;

        if (myUser.getRUB_wallet() != null) {
            type = "RUB";
            UserReplenishWalletRubDto userRub = new UserReplenishWalletRubDto();
            userRub.setRUB_wallet(userService.userReplenishmentWallet(myUser.getSecretKey(), type, myUser.getRUB_wallet()).toString());
            return userRub;
        } else if (myUser.getTON_wallet() != null) {
            type = "TON";
            UserReplenishWalletTonDto userTon = new UserReplenishWalletTonDto();
            userTon.setTON_wallet(userService.userReplenishmentWallet(myUser.getSecretKey(), type, myUser.getTON_wallet()).toString());
            return userTon;
        } else if (myUser.getBTC_wallet() != null){
            type = "BTC";
            UserReplenishWalletBtcDto userBtc = new UserReplenishWalletBtcDto();
            userBtc.setBTC_wallet(userService.userReplenishmentWallet(myUser.getSecretKey(), type, myUser.getBTC_wallet()).toString());
            return userBtc;
        }

        Date date = new Date();
        return new ErrorDto("Wallet with this type does not exist.", date);
    }

    @PostMapping("/get")
    public Object postGetMoneyFromMarket(@RequestBody UserGetMoneyMarketDto myUser) {

        if (myUser.getCredit_card() != null) {

            UserReplenishWalletRubDto userRub = new UserReplenishWalletRubDto();
            userRub.setRUB_wallet(userService.userGetMoneyFromMarket(myUser.getSecretKey(), myUser.getCurrency(),
                    myUser.getCount(), myUser.getCredit_card()).toString());
            return userRub;

        } else if (myUser.getWallet() != null) {

            if (Objects.equals(myUser.getCurrency(), "TON")) {

                UserReplenishWalletTonDto userTon = new UserReplenishWalletTonDto();
                userTon.setTON_wallet(userService.userGetMoneyFromMarket(myUser.getSecretKey(), myUser.getCurrency(),
                        myUser.getCount(), myUser.getWallet()).toString());
                return userTon;

            } else if (Objects.equals(myUser.getCurrency(), "BTC")){

                UserReplenishWalletBtcDto userBtc = new UserReplenishWalletBtcDto();
                userBtc.setBTC_wallet(userService.userGetMoneyFromMarket(myUser.getSecretKey(), myUser.getCurrency(),
                        myUser.getCount(), myUser.getWallet()).toString());
                return userBtc;

            }
        }

        Date date = new Date();
        return new ErrorDto("Crypto - input wallet, money - input credit_card!", date);
    }

    @GetMapping("/rate")
    public Object getWatchRateUser(@RequestBody UserCurrencyDto myUser) {
        GetCurrencyDto cur = new GetCurrencyDto();
        HashMap<String, String> hashMapRate = userService.userGetActualCourse(myUser.getSecretKey(), myUser.getCurrency());

        switch (myUser.getCurrency()) {
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


    @PostMapping("/exchange")
    public Object postExchangeMoney(@RequestBody ExchangeValueDto myUser) {

        HashMap<String, String> exchangedValue = userService.userExchangeValue(myUser.getSecretKey(),
                myUser.getCurrency_from(), myUser.getCurrency_to(), myUser.getAmount());

        AlreadyExchangedValueDto exchangedValueDto = new AlreadyExchangedValueDto();

        exchangedValueDto.setCurrency_from(exchangedValue.get("currency from"));
        exchangedValueDto.setCurrency_to(exchangedValue.get("currency to"));
        exchangedValueDto.setAmount_from(exchangedValue.get("amount from"));
        exchangedValueDto.setAmount_to(exchangedValue.get("amount to"));

        return exchangedValueDto;
    }
}
