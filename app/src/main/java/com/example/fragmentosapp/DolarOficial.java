package com.example.fragmentosapp;

import java.util.Date;

public class DolarOficial {
    private String dolarFecha; // hacerlo date para comparar
    private String dolarCompra;
    private String dolarVenta;


    public DolarOficial(String dolarFecha, String dolarCompra, String dolarVenta){
        this.dolarFecha = dolarFecha;
        this.dolarCompra = dolarCompra;
        this.dolarVenta = dolarVenta;
    }


    public String getDolarFecha() {
        return dolarFecha;
    }

    public void setDolarFecha(String dolarFecha) {
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

