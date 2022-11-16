package com.santander.homebanking.dtos;

import com.santander.homebanking.models.Account;
import com.santander.homebanking.models.LongTermIncome;
import com.santander.homebanking.models.PeriodType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LongTermIncomeDTO {

    private Long id;
    private PeriodType periodType;
    private long accountId;
    private Double amount;
    private String account;
    private LocalDateTime date;
    private Integer days;
    private Boolean isActive;
    private LocalDateTime paymentDay;
    private Integer daysLeft;
    private Double profit;
    public LongTermIncomeDTO() {}

    public LongTermIncomeDTO(PeriodType periodType, long accountId, Double amount) {
        this.periodType = periodType;
        this.accountId = accountId;
        this.amount = amount;
    }

    public LongTermIncomeDTO(LongTermIncome longTermIncome) {
        this.id = longTermIncome.getId();
        this.days = longTermIncome.getPeriod().getDays();
        this.accountId = longTermIncome.getAccount().getId();
        this.amount = longTermIncome.getAmount();
        this.account = longTermIncome.getAccount().getNumber();
        this.date = longTermIncome.getCreationDate();
        this.isActive = longTermIncome.getActive();
        this.paymentDay = longTermIncome.getCreationDate().plusDays( longTermIncome.getPeriod().getDays());
        this.daysLeft= Math.toIntExact(ChronoUnit.DAYS.between(LocalDateTime.now(), this.paymentDay));
        this.profit = amount * longTermIncome.getPeriod().getPercentage();
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

    public String getDate() {
        String date1= date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return date1;
    }

    public Long getId() {
        return id;
    }

    public Integer getDays() {
        return days;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Integer getDaysLeft() {
        return daysLeft;
    }

    public Double getProfit() {
        return profit;
    }

    public String getPaymentDay() {
        String payment= paymentDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return payment;
    }
}
