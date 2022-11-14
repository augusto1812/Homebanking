package com.santander.homebanking.models;

public enum PeriodType {

    PERIOD_30( 5.5 ),
    PERIOD_60( 11.0 ),
    PERIOD_90(16.5 ),
    PERIOD_180( 33.0 ),
    PERIOD_365( 66.0 );

    private Double percentage;

    PeriodType(Double percentage){
        this.percentage = percentage;
    }

}
