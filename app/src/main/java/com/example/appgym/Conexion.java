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
    String local ="jdbc:jtds:sqlserver://IP donde esta alojada la BD:1433;databaseName=GYM;user=....;password=....;loginTimeout=1;";
    String remoto = "jdbc:jtds:sqlserver://IP publica:1433;databaseName=GYM;user=....;password=....;loginTimeout=1;";

    public Connection Conexion(){

        try {
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            acceso = DriverManager.getConnection(local);
               PreparedStatement ps =   acceso.prepareStatement("select * from Clientes");
                int rs = ps.executeUpdate();
                if(rs <=0){
                    acceso=null;
                    acceso = DriverManager.getConnection(remoto);
                }
        } catch (Exception e) {
           System.out.println(""+e);
        }
        return acceso;
    }


}
