package com.example.fragmentosapp;

import java.time.LocalDate;

public class DolarHistorico {
    private LocalDate dolarFecha;
    private String dolarCompra;
    private String dolarVenta;


    public DolarHistorico(LocalDate dolarFecha, String dolarCompra, String dolarVenta){
        this.dolarFecha = dolarFecha;
        this.dolarCompra = dolarCompra;
        this.dolarVenta = dolarVenta;
    }



    public LocalDate getDolarFecha() {
        return dolarFecha;
    }

    public void setDolarFecha(LocalDate dolarFecha) {
        this.dolarFecha = dolarFecha;
    }

    public String getDolarCompra() {
        return dolarCompra;
    }

    public void setDolarCompra(String dolarCompra) {
        this.dolarCompra = dolarCompra;
    }

    public String getDolarVenta() {
        return dolarVenta;
    }

    public void setDolarVenta(String dolarVenta) {
        this.dolarVenta = dolarVenta;
    }
}

