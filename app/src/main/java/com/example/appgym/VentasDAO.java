package com.example.appgym;

import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.example.appgym.Ventas_Activity;

public class VentasDAO {
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn=new Conexion();
    Connection con;
    int r=0;
    int rr=0;

    public String SerieVenta(){
        String serie="";
        String sql="SELECT MAX(NumeroVenta) FROM Ventas";
        try{
            con=cn.Conexion();
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next())
                serie=rs.getString(1);
        } catch (Exception e) {
System.out.println("-----------------------------------------------------------------------------------------------"+e);
        }
        return serie;
    }


    public String IdVentas(){
        String idv="";
        String sql="SELECT MAX(Id_Venta) FROM Ventas";
        try {
            con=cn.Conexion();
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next())
                idv=rs.getString(1);
        } catch (Exception e) {
            System.out.println("-----------------------------------------------------------------------------------------------"+e);
        }
        return idv;
    }
    public int GuardarVentas(Venta v){
        Venta Venta=new Venta();
        String sql="INSERT INTO Ventas(Id_Entrenador,NumeroVenta,FechaVenta,Monto,Estado) Values (?,?,GETDATE(),?,?)";
        try {
            con=cn.Conexion();
            ps=con.prepareStatement(sql);
            ps.setInt(1, v.getIdEntrenador());
            ps.setString(2, v.getSerie());
            ps.setDouble(3, v.getMonto());
            ps.setString(4, v.getEstado());
            r=ps.executeUpdate();
            if (r > 0){

                System.out.println("-----------------------------------------------------------------------------------------------"+"GuardaVentas");
            }

        } catch (Exception e){
            System.out.println("-----------------------------------------------------------------------------------------------"+e);
        }
        return r;
    }
    public int GuardarDetalleVentas(DetalleVentas dv){

        String sql="INSERT INTO VentasDetalle(Id_Venta,Id_Producto,Unidad,Cantidad,PrecioVenta) Values (?,?,?,?,?)";
        try {
            con=cn.Conexion();
            ps=con.prepareStatement(sql);
            ps.setInt(1, dv.getIdVenta());
            ps.setInt(2, dv.getIdPro());
            ps.setString(3, dv.getUnidad());
            ps.setInt(4, dv.getCantidad());
            ps.setDouble(5, dv.getPreventa());
            r=ps.executeUpdate();
            if (r > 0){

                System.out.println("-----------------------------------------------------------------------------------------------"+"GDetalle");
            }
        } catch (Exception e){
            System.out.println("-----------------------------------------------------------------------------------------------"+e);
        }
        return rr;
    }
}
