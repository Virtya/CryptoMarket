package com.virtya.CryptoMarket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.virtya.CryptoMarket.entity.Currency;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Data
@AllArgsConstructor
public class AdminModel {
    @JsonProperty(value = "secretKey")
    private Long secretKey;

    @JsonProperty(value = "currencies")
    private List<Currency> currencies;
}
