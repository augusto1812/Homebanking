package com.santander.homebanking.models;

public enum PeriodType {

    PERIOD_30( 30,0.055 ),
    PERIOD_60(60, 0.11 ),
    PERIOD_90( 90,0.165 ),
    PERIOD_180( 180,0.33 ),
    PERIOD_365( 365,0.66 );

    private Integer days;
    private Double percentage;

    PeriodType(Integer days, Double percentage){
        this.days = days;
        this.percentage = percentage;
    }

    public Integer getDays() {
        return days;
    }
    public Double getPercentage() {
        return percentage;
    }
}
