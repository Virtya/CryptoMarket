package com.virtya.CryptoMarket.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class OurUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ourUser")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String secretKey;

    @ManyToMany(mappedBy = "ourUsers")
    private List<Currency> currencies;

    @NotNull
    private Double RUB_balance;

    @NotNull
    private Double TON_balance;

    @NotNull
    private Double BTC_balance;
}
