package com.santander.homebanking.dtos;

public class CurrencyDTO {

    private Double compra;

    private Double venta;

    public CurrencyDTO() {
    }

    public CurrencyDTO(Double compra, Double venta) {
        this.compra = compra;
        this.venta = venta;
    }

    public Double getCompra() {
        return compra;
    }

    public void setCompra(Double compra) {
        this.compra = compra;
    }

    public Double getVenta() {
        return venta;
    }

    public void setVenta(Double venta) {
        this.venta = venta;
    }
}
