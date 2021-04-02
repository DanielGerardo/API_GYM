package com.example.appgym;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;

import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class Clientes {
    int id;
    String nom;
    String ape;
    String dire;
    int tel;
    int telA;
    String sexo;
    int edad;
    String fecha;
    byte[] foto;
    String user;
    int activo;
    String fplan;

    public Clientes() {
    }

    public Clientes(int id, String nom, String ape, String dire, int tel, int telA, String sexo, int edad, String fecha,byte[] foto, String user, int activo, String fplan) {
        this.id = id;
        this.nom = nom;
        this.ape = ape;
        this.dire = dire;
        this.tel = tel;
        this.telA = telA;
        this.sexo = sexo;
        this.edad = edad;
        this.fecha = fecha;
        this.foto = foto;
        this.user = user;
        this.activo = activo;
        this.fplan = fplan;
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

    public String getApe() {
        return ape;
    }

    public void setApe(String ape) {
        this.ape = ape;
    }

    public String getDire() {
        return dire;
    }

    public void setDire(String dire) {
        this.dire = dire;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public int getTelA() {
        return telA;
    }

    public void setTelA(int telA) {
        this.telA = telA;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public byte[] getFoto() {
        return foto;
    }

    public Bitmap setFoto(byte[] foto) {
        this.foto = foto;
        Bitmap f = BitmapFactory.decodeByteArray(foto,0,foto.length);
        return f;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFplan() {
        return fplan;
    }

    public void setFplan(String fplan) {
        this.fplan = fplan;
    }



    public void setActivo(int activo) {
        this.activo = activo;
    }



    public String toString(){
        return this.user;
    }




}
