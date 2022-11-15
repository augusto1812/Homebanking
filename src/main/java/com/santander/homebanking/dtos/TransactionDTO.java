package com.santander.homebanking.dtos;
import com.santander.homebanking.models.Transaction;
import com.santander.homebanking.models.TransactionType;
import java.time.LocalDateTime;

import static com.santander.homebanking.utils.MathUtils.round;

public class TransactionDTO {

    private long id;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;

    public TransactionDTO(){}

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return
        round(amount);
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
