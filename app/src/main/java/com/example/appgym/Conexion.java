package com.example.appgym;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conexion extends Exception{


    Connection acceso = null;
    String local ="jdbc:jtds:sqlserver://192.168.1.78:1433;databaseName=GYM;user=da;password=1234;loginTimeout=1;";
    String remoto = "jdbc:jtds:sqlserver://akolenii.gotdns.ch:1433;databaseName=GYM;user=da;password=1234;loginTimeout=1;";

    public Connection Conexion(){

        try {
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            acceso = DriverManager.getConnection(local);
        } catch (Exception e) {
            try {
                acceso=null;
                StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(politica);
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                acceso = DriverManager.getConnection(remoto);
            } catch (Exception ex) {

            }
        }
        return acceso;
    }


}
