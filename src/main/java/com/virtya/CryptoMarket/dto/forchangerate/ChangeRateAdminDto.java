package com.virtya.CryptoMarket.dto.forchangerate;

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
public class ChangeRateAdminDto {
    private String secretKey;
    private String base_currency;
    @JsonProperty(value = "RUB")
    private String RUB;
    @JsonProperty(value = "TON")
    private String TON;
    @JsonProperty(value = "BTC")
    private String BTC;
}
