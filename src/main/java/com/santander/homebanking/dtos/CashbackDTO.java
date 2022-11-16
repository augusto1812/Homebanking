package com.santander.homebanking.dtos;

import com.santander.homebanking.models.CardColor;
import com.santander.homebanking.models.CardType;
import com.santander.homebanking.models.Cashback;

import javax.persistence.Column;

public class CashbackDTO {

    private long id;
    private CardColor cardColor;
    private CardType cardType;
    private Double percentage;

    public CashbackDTO(Cashback cashback) {
        this.id = cashback.getId();
        this.cardColor = cashback.getCardColor();
        this.cardType = cashback.getCardType();
        this.percentage = cashback.getPercentage();
    }

    public long getId() {
        return id;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Double getPercentage() {
        return percentage;
    }
}
