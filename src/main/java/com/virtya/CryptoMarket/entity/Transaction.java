package com.virtya.CryptoMarket.entity;


import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Date date;
}
