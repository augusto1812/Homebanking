package com.santander.homebanking.dtos;

import com.santander.homebanking.models.Account;
import com.santander.homebanking.models.PeriodType;

public class LongTermIncomeDTO {


    private PeriodType periodType;
    private long accountId;
    private Double amount;

    public LongTermIncomeDTO() {}

    public LongTermIncomeDTO(PeriodType periodType, long accountId, Double amount) {
        this.periodType = periodType;
        this.accountId = accountId;
        this.amount = amount;
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
}
