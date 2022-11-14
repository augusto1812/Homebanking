package com.santander.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class LongTermIncome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
    private Double amount;
    private PeriodType period;
    @CreatedDate
    @Column(name="creation_date")
    private LocalDateTime creationDate;
    private Double minAmount = 1000.00;
    private Boolean isActive = true;

    public LongTermIncome() {
    }

    public LongTermIncome(Account account, Double amount, PeriodType period) {
        this.account = account;
        this.amount = amount;
        this.period = period;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PeriodType getPeriod() {
        return period;
    }
    public void setPeriod(PeriodType period) {
        this.period = period;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public Boolean getActive() {
        return isActive;
    }
    public void setActive(Boolean active) {
        isActive = active;
    }
}
