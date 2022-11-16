package com.santander.homebanking.dtos;

public class CurrencyCryptoDTO {

    private Double ask;

    private Double totalAsk;

    private Double bid;

    private Double totalBid;

    private Integer time;

    public CurrencyCryptoDTO() {
    }

    public CurrencyCryptoDTO(Double ask, Double totalAsk, Double bid, Double totalBid, Integer time) {
        this.ask = ask;
        this.totalAsk = totalAsk;
        this.bid = bid;
        this.totalBid = totalBid;
        this.time = time;
    }

    public CurrencyCryptoDTO(Integer ask, Integer totalAsk, Integer bid, Integer totalBid, Integer time) {
        this.ask = Double.valueOf(ask);
        this.totalAsk = Double.valueOf(totalAsk);
        this.bid = Double.valueOf(bid);
        this.totalBid = Double.valueOf(totalBid);
        this.time = time;
    }

    public Double getAsk() {
        return ask;
    }

    public void setAsk(Double ask) {
        this.ask = ask;
    }

    public Double getTotalAsk() {
        return totalAsk;
    }

    public void setTotalAsk(Double totalAsk) {
        this.totalAsk = totalAsk;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getTotalBid() {
        return totalBid;
    }

    public void setTotalBid(Double totalBid) {
        this.totalBid = totalBid;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
