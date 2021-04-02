package com.example.appgym;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductosDAO {
    int r;
    PreparedStatement ps;
    ResultSet rs;
    Conexion con=new Conexion();
    Connection acceso;
    Productos pro=new Productos();

    public int actulizarStock(int cant, int idp){
        String sql="UPDATE Productos set Cantidad=? WHERE Id_Producto=?";
        try {
            acceso=con.Conexion();
            ps=acceso.prepareStatement(sql);
            ps.setInt(1, cant);
            ps.setInt(2, idp);
            ps.executeUpdate();
        } catch (SQLException e){

        }
        return r;
    }

    public Productos listarID(int Id) {
        Productos p=new Productos();
        String sql="SELECT * FROM Productos WHERE Id_Producto=?";
        try {
            acceso=con.Conexion();
            ps=acceso.prepareStatement(sql);
            ps.setInt(1, Id);
            rs=ps.executeQuery();
            while (rs.next()) {
                p.setId(rs.getInt(1));
                p.setDes(rs.getString(2));
                p.setUnidad(rs.getString(3));
                p.setCan(rs.getInt(4));
                p.setPrecio(rs.getDouble(5));
                p.setFecha(rs.getString(6));
            }
        } catch (SQLException ex) {
            System.out.println("Error de conexion");


        }
        return p;
    }
}
