package com.virtya.CryptoMarket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.virtya.CryptoMarket.entity.Admin;
import com.virtya.CryptoMarket.entity.OurUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CurrencyModel {
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "rate")
    private Double rate;

    @JsonProperty(value = "users")
    private List<OurUser> users;

    @JsonProperty(value = "admins")
    private List<Admin> admins;
}
