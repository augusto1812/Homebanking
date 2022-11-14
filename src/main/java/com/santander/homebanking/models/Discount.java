package com.santander.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;

    private SectorType sectorType;

    @Column(name = "from_date")
    private LocalDateTime fromDate;

    @Column(name = "final_date")
    private LocalDateTime finalDate;


    @ElementCollection
    @MapKeyColumn(name = "card_type")
    @Column(name = "percentage_per_card")
    private Map<CardColor,Double> percentagePerCard = new HashMap<>();

    public Discount() {}

    public Discount(SectorType sectorType, LocalDateTime fromDate, LocalDateTime finalDate, Map<CardColor, Double> percentagePerCard) {
        this.sectorType = sectorType;
        this.fromDate = fromDate;
        this.finalDate = finalDate;
        this.percentagePerCard = percentagePerCard;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SectorType getSectorType() {
        return sectorType;
    }

    public void setSectorType(SectorType sectorType) {
        this.sectorType = sectorType;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDateTime finalDate) {
        this.finalDate = finalDate;
    }

    public Map<CardColor, Double> getPercentagePerCard() {
        return percentagePerCard;
    }

    public void setPercentagePerCard(Map<CardColor, Double> percentagePerCard) {
        this.percentagePerCard = percentagePerCard;
    }
}
