package com.virtya.CryptoMarket.dto.forgetmoney;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class UserGetMoneyMarketDto {
    private String secretKey;
    private String currency;
    private String count;
    @JsonProperty(value = "credit_card")
    private String credit_card;
    @JsonProperty(value = "wallet")
    private String wallet;
}
