package com.virtya.CryptoMarket.dto.forexchangemoney;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ExchangeValueDto {
    private String secretKey;
    private String currency_from;
    private String currency_to;
    private String amount;
}
