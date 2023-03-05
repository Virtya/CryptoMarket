package com.virtya.CryptoMarket.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class ErrorDto {

    private String message;

    private Date timestamp;
}
