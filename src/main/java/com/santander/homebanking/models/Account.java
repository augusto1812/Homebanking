package com.santander.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;
    private String number;
    @CreatedDate
    @Column(name="creation_date")
    private LocalDateTime creationDate;
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch= FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy="account", fetch= FetchType.EAGER)
    Set<DailyIncome> dailysIncomes = new HashSet<>();

    @OneToMany(mappedBy="account", fetch= FetchType.EAGER)
    Set<LongTermIncome> longTermIncomes= new HashSet<>();

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }


    public Account(){}

    public Account(String number, double balance, Client client) {
        this.number = number;
        this.balance = balance;
        this.client = client;
    }
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    //Borrar DESPUES
    public Set<LongTermIncome> getLongTermIncomes() {
        return longTermIncomes;
    }
}
