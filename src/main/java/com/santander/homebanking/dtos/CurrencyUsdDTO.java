package com.santander.homebanking.dtos;

public class CurrencyUsdDTO {

    private String compra;

    private String venta;

    private String fecha;

    private String variacion;

    private String class_variacion;


    public CurrencyUsdDTO() {
    }

    public CurrencyUsdDTO(String compra, String venta, String fecha, String variacion, String class_variacion) {
        this.compra = compra;
        this.venta = venta;
        this.fecha = fecha;
        this.variacion = variacion;
        this.class_variacion = class_variacion;
    }

    public String getCompra() {
        return compra;
    }

    public void setCompra(String compra) {
        this.compra = compra;
    }

    public String getVenta() {
        return venta;
    }

    public void setVenta(String venta) {
        this.venta = venta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getVariacion() {
        return variacion;
    }

    public void setVariacion(String variacion) {
        this.variacion = variacion;
    }

    public String getClass_variacion() {
        return class_variacion;
    }

    public void setClass_variacion(String class_variacion) {
        this.class_variacion = class_variacion;
    }
}
