package com.santander.homebanking.dtos;

import com.santander.homebanking.models.Card;
import com.santander.homebanking.models.SectorType;

public class ShopDTO {

    private Long cardId;
    private SectorType sectorType;
    private Double amount;

    public ShopDTO(Long cardId, SectorType sectorType, Double amount) {
        this.cardId = cardId;
        this.sectorType = sectorType;
        this.amount = amount;
    }

    public Long getCardId() {
        return cardId;
    }

    public SectorType getSectorType() {
        return sectorType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public void setSectorType(SectorType sectorType) {
        this.sectorType = sectorType;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
