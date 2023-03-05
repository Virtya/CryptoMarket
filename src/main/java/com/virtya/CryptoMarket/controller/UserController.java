package com.virtya.CryptoMarket.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtya.CryptoMarket.dto.*;
import com.virtya.CryptoMarket.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
        UserGetBalanceDto user = objectMapper.readValue(myUser, UserGetBalanceDto.class);

        List<Double> userBalance;

        userBalance = userService.userWatchBalance(user.getSecretKey());
        WalletDto wallet = new WalletDto();
        wallet.setRUB_wallet(userBalance.get(0).toString());
        wallet.setTON_wallet(userBalance.get(1).toString());
        wallet.setBTC_wallet(userBalance.get(2).toString());

        return wallet;
    }

    @PostMapping("/replenish")
    public Object postReplenishWallet(@RequestBody UserGetBalanceDto myUser) throws JsonProcessingException, JSONException {

        String type;
        if (myUser.getRUB_wallet() != null) {
            type = "RUB";
            return "RUB_wallet: " + userService.userReplenishmentWallet(myUser.getSecretKey(), type, myUser.getRUB_wallet());
        } else if (myUser.getTON_wallet() != null) {
            type = "TON";
            return "TON_wallet: "  + userService.userReplenishmentWallet(myUser.getSecretKey(), type, myUser.getTON_wallet());
        } else if (myUser.getBTC_wallet() != null){
            type = "BTC";
            return "BTC_wallet: " + userService.userReplenishmentWallet(myUser.getSecretKey(), type, myUser.getBTC_wallet());
        }
        return "Wallet with this type does not exist.";
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
