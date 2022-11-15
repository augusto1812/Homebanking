package com.santander.homebanking.dtos;

import com.santander.homebanking.models.CardColor;
import com.santander.homebanking.models.Discount;
import com.santander.homebanking.models.SectorType;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.MapKeyColumn;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DiscountDTO {

    private long id;
    private SectorType sectorType;
    private LocalDateTime fromDate;
    private LocalDateTime finalDate;
    private Map<CardColor,Double> percentagePerCard = new HashMap<>();

    public DiscountDTO(Discount discount) {
        this.id = discount.getId();
        this.sectorType = discount.getSectorType();
        this.fromDate = discount.getFromDate();
        this.finalDate = discount.getFinalDate();
        this.percentagePerCard = discount.getPercentagePerCard();
    }

    public long getId() {
        return id;
    }

    public SectorType getSectorType() {
        return sectorType;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getFinalDate() {
        return finalDate;
    }

    public Map<CardColor, Double> getPercentagePerCard() {
        return percentagePerCard;
    }
}
