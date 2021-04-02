package com.example.appgym;


public class ClientePlan {
    int id;
    int user;
    int idC;
    int idP;
    String fechaC;
    String fechaV;
    int uti;
    double precio;

    public ClientePlan() {
    }

    public ClientePlan(int id, int user, int idC, int idP, String fechaC, String fechaV, int uti, double precio) {
        this.id = id;
        this.user = user;
        this.idC = idC;
        this.idP = idP;
        this.fechaC = fechaC;
        this.fechaV = fechaV;
        this.uti = uti;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public int getIdP() {
        return idP;
    }

    public void setIdP(int idP) {
        this.idP = idP;
    }

    public String getFechaC() {
        return fechaC;
    }

    public void setFechaC(String fechaC) {
        this.fechaC = fechaC;
    }

    public String getFechaV() {
        return fechaV;
    }

    public void setFechaV(String fechaV) {
        this.fechaV = fechaV;
    }

    public int getUti() {
        return uti;
    }

    public void setUti(int uti) {
        this.uti = uti;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
