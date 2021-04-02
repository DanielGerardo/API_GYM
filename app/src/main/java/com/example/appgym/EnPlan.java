package com.example.appgym;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class EnPlan {
    int id;
    String nom;
    int dias;
    double valor;
    String obs;

    public EnPlan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String toString(){
        return this.nom;
    }

    public Vector<EnPlan> mostrarPlan() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Conexion con = new Conexion();
        Connection acceso;

        Vector<EnPlan> datos = new Vector<EnPlan>();
        EnPlan dat = null;


        try {
            acceso = con.Conexion();
            String sql = "SELECT * FROM Planes";
            ps = acceso.prepareStatement(sql);
            rs = ps.executeQuery();
            dat = new EnPlan();
            dat.setId(0);
            dat.setNom("Selecciona Plan");
            dat.setDias(3);
            dat.setValor(4);
            datos.add(dat);

            while (rs.next()) {
                dat = new EnPlan();
                dat.setId(rs.getInt("Id_Plan"));
                dat.setNom(rs.getString("Nombre"));
                dat.setDias(rs.getInt("Dias"));
                dat.setValor(rs.getDouble("valor"));
                datos.add(dat);

            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");


        }
        return datos;
    }
}
