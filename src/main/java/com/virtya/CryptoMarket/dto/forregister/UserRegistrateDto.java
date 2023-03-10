package com.virtya.CryptoMarket.dto.forregister;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class UserRegistrateDto {
    private String username;
    private String email;
}
