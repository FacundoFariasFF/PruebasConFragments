package com.example.fragmentosapp;

public class Monedas {
    String nombre;
    String compra;
    String venta;
    String fecha;

    public Monedas(String nombre, String compra, String venta, String fecha){
        this.nombre = nombre;
        this.compra = compra;
        this.venta = venta;
        this.fecha= fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
}

