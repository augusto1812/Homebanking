package com.santander.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class DailyIncome {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;


}
