package com.santander.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;

    private String name;
    @Column(name="MAX_AMOUNT")
    private Integer maxAmount;
    @ElementCollection
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch= FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Loan(String name, Integer maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public Loan() {}

    @JsonIgnore
    public Set<Client> getClient() {
        return clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }


}
