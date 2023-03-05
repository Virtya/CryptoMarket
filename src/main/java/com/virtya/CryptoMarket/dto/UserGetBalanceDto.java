package com.virtya.CryptoMarket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class UserGetBalanceDto {
    private String secretKey;

    @JsonProperty(value = "RUB_wallet")
    private String RUB_wallet;
    @JsonProperty(value = "TON_wallet")
    private String TON_wallet;
    @JsonProperty(value = "BTC_wallet")
    private String BTC_wallet;
}