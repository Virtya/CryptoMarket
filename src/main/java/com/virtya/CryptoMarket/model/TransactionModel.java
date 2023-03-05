package com.virtya.CryptoMarket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.virtya.CryptoMarket.entity.Admin;
import com.virtya.CryptoMarket.entity.OurUser;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@Data
@AllArgsConstructor
public class TransactionModel {
    @JsonProperty(value = "date")
    private Date date;

    @JsonProperty(value = "ourUser")
    private OurUser user;

    @JsonProperty(value = "admin")
    private Admin admin;
}
