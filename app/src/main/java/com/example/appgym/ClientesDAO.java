package com.example.appgym;


import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import com.example.appgym.PlanesClientes;
import static java.time.temporal.ChronoUnit.DAYS;


public class ClientesDAO {
    String r = "";
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();
    Connection con;
   int rr=0;

        public int GuardarPlan (ClientePlan v){
            ClientePlan ClientePlan = new ClientePlan();
            String sql = "INSERT INTO ClientePlan (id_Entrenador,Id_Cliente,Id_Plan,FechaCompra,FechaVencimiento,monto) Values (?,?,?,?,?,?)";
            try {
                con = cn.Conexion();
                ps = con.prepareStatement(sql);
                ps.setInt(1, v.getUser());
                ps.setInt(2, v.getIdC());
                ps.setInt(3, v.getIdP());
                ps.setString(4, v.getFechaC());
                ps.setString(5, v.getFechaV());
                ps.setDouble(6, v.getPrecio());
                rr = ps.executeUpdate();
                if (rr > 0) {
                    System.out.println("jalo¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨");
                }
            } catch (SQLException e) {
                System.out.println(e+"-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            }
            return rr;

        }

}
