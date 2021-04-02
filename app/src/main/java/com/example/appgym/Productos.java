package com.example.appgym;

import android.widget.ArrayAdapter;

public class Productos {

    int id;
    String des;
    String unidad;
    int can;
    double precio;
    String fecha;

    public Productos() {
    }

    public Productos(int id, String des, String unidad, int can, double precio, String fecha) {
        this.id = id;
        this.des = des;
        this.unidad = unidad;
        this.can = can;
        this.precio = precio;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getCan() {
        return can;
    }

    public void setCan(int can) {
        this.can = can;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String toString() {
        return this.des;
    }


}
