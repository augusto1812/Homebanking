package com.santander.homebanking.dtos;

import com.santander.homebanking.models.Account;
import com.santander.homebanking.models.LongTermIncome;
import com.santander.homebanking.models.PeriodType;

import java.time.LocalDateTime;

public class LongTermIncomeDTO {


    private PeriodType periodType;
    private long accountId;
    private Double amount;
    private String account;
    private LocalDateTime date;
    private Integer days;
    public LongTermIncomeDTO() {}

    public LongTermIncomeDTO(PeriodType periodType, long accountId, Double amount) {
        this.periodType = periodType;
        this.accountId = accountId;
        this.amount = amount;
    }

    public LongTermIncomeDTO(LongTermIncome longTermIncome) {
        this.days = longTermIncome.getPeriod().getDays();
        this.accountId = longTermIncome.getAccount().getId();
        this.amount = longTermIncome.getAmount();
        this.account = longTermIncome.getAccount().getNumber();
        this.date = longTermIncome.getCreationDate();
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountID(Long accountId) {
        this.accountId = accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Integer getDays() {
        return days;
    }
}
