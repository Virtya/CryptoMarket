package com.virtya.CryptoMarket.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtya.CryptoMarket.dto.*;
import com.virtya.CryptoMarket.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/user", produces =  "application/json")
public class UserController {

    private final UserService userService;

    @PostMapping("/registrate")
    public Object postRegistrationUser(@RequestBody String myUser) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserRegistrateDto user = objectMapper.readValue(myUser, UserRegistrateDto.class);
        UserDto usr = new UserDto();
        usr.setSecretKey(userService.userRegistrate(user.getUsername(), user.getEmail()));
        return usr;
    }

    @GetMapping("/watch")
    public Object getWatchBalanceUser(@RequestBody String myUser) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserReplenishWalletDto user = objectMapper.readValue(myUser, UserReplenishWalletDto.class);

        List<Double> userBalance;

        userBalance = userService.userWatchBalance(user.getSecretKey());
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
                UserReplenishWalletBtcDto userTon = new UserReplenishWalletBtcDto();
                userTon.setBTC_wallet(userService.userGetMoneyFromMarket(myUser.getSecretKey(), myUser.getCurrency(),
                        myUser.getCount(), myUser.getWallet()).toString());
                return userTon;
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

    /*@PutMapping("/{id}")
    public ResponseEntity<Currency> updateCountry(@PathVariable Long id, @RequestBody AdminModel countryModel)
    {
        return new ResponseEntity<>(countryService.updateCountry(id, countryModel), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteById(@PathVariable Long id)
    {
        countryService.deleteById(id);
    }*/
}
