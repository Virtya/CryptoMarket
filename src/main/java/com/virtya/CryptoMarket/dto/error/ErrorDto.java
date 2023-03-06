package com.virtya.CryptoMarket.dto.error;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@AllArgsConstructor
@Data
public class ErrorDto {
    private String message;

    private Date timestamp;
}
