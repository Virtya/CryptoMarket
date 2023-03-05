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

    @JoinColumn(name = "id_ourUser")
    @ManyToOne
    private OurUser ourUser;

    @JoinColumn(name = "id_admin")
    @ManyToOne
    private Admin admin;
}
