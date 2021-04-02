package com.example.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class Historial_activity extends AppCompatActivity {
    private TableLayout tl1;

  public TableDynamic tableDynamic = new TableDynamic();

    private Object[]header={"Movimientos", "FECHA Y HORA", "ID_Producto", "Descripcion", "Unidad", "Cantidad"};
    private ArrayList<Object[]> rows=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        TableDynamic.tableLayout = (TableLayout) findViewById(R.id.table_historial);
        TableDynamic.context = getApplicationContext();
        cargartable();

    }
    public TableLayout cargartable(){
        tableDynamic.addHeader(header);
        tableDynamic.addData(gethistorial());
        tableDynamic.backgroundHeader(Color.rgb(0, 149, 255));
        tableDynamic.textColorHeader(Color.rgb(0, 0, 0));
        tableDynamic.backgroundHistorial(0,Color.rgb(250,100,100),Color.rgb(100,250,100));
        return TableDynamic.tableLayout;
    }
    public ArrayList<Object[]>gethistorial() {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;
        int colu;

        try {
            ps = cn.prepareStatement("SELECT movimiento,fecha,id_producto,descripcion,unidad,cantidad FROM historial");
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            colu = rsmd.getColumnCount();

            while (rs.next()) {

                Object[] fila = new Object[colu];
                for (int indice = 0; indice < colu; indice++) {
                    fila[indice] = rs.getString(indice + 1);

                }

                rows.add((Object[]) fila);

            }


        } catch (SQLException ex) {
            Toast.makeText(this,"Error Tabla historial"+ex,Toast.LENGTH_SHORT).show();
        }
        return rows;
    }
}