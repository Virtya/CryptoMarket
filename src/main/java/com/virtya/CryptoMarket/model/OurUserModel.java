package com.virtya.CryptoMarket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.virtya.CryptoMarket.entity.Currency;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Data
@AllArgsConstructor
public class OurUserModel {
    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "secretKey")
    private String secretKey;

    @JsonProperty(value = "currencies")
    private List<Currency> currencies;

    @NotNull
    @JsonProperty(value = "RUB_balance")
    private Double RUB_balance;

    @NotNull
    @JsonProperty(value = "TON_balance")
    private Double TON_balance;

    @NotNull
    @JsonProperty(value = "BTC_balance")
    private Double BTC_balance;
}
