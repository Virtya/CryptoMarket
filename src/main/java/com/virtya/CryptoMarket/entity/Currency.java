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
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private Double rate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "oruUser_currency",
            joinColumns = @JoinColumn(name = "id_ourUser"),
            inverseJoinColumns = @JoinColumn(name = "id_currency"))
    private List<OurUser> ourUsers;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "admin_currency",
            joinColumns = @JoinColumn(name = "id_admin"),
            inverseJoinColumns = @JoinColumn(name = "id_currency"))
    private List<Admin> admins;
}
