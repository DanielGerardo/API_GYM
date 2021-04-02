package com.example.appgym;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.appgym.TableDynamic.data;
import static com.example.appgym.TableDynamic.tableLayout;

public class Vencimientos extends AppCompatActivity {
    private Object[] header = {"User","Nombre","Apellidos","Vencimiento"};
    private TextView fechavenci;
    public TableDynamic tableDynamic = new TableDynamic();
    private ArrayList<Object[]> rows = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vencimientos);

        fechavenci =(TextView)findViewById(R.id.fecha_vencimiento);
        TableDynamic.tableLayout = (TableLayout) findViewById(R.id.table_Vencimiento);
        TableDynamic.context = getApplicationContext();
        fechavenci.setText(fechaActual());
        cargartable();
    }
    public static String fechaActual(){
        Date fecha = new Date();
        DateFormat formatofecha = new SimpleDateFormat("yyyy-MM-dd");
        return formatofecha.format(fecha);

    }
    public TableLayout cargartable(){
        tableDynamic.addHeader(header);
        tableDynamic.addData(getfecha());
        tableDynamic.backgroundHeader(Color.parseColor("#FF9800"));
        tableDynamic.textColorHeader(Color.rgb(0, 0, 0));
        tableDynamic.textColorData(Color.parseColor("#FF9800"));
        return TableDynamic.tableLayout;
    }
    public TableLayout recargartable(){
        tableLayout.removeViewsInLayout(1,data.size());
        tableDynamic.addDatanull();
        tableLayout.removeViewsInLayout(1,data.size());
        data.clear();
        tableDynamic.addData(getfecha());
        tableDynamic.textColorData(Color.parseColor("#FF9800"));
        return TableDynamic.tableLayout;
    }
    public ArrayList<Object[]> getfecha() {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;
        String fee = fechavenci.getText().toString();
        int colu;
        try {
            ps = cn.prepareStatement("SELECT UserName,Nombre,Apellidos,fechaPlan FROM Clientes WHERE fechaPlan=?");
            ps.setString(1, fee);
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            colu = rsmd.getColumnCount();
            while (rs.next()) {
                Object[] fila = new Object[colu];
                for (int indice=0; indice < colu; indice++) {
                    fila[indice] = rs.getString(indice + 1);
                }
                rows.add((Object[]) fila);
            }
        } catch (SQLException ex) {
            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();
        }
        return rows;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void fechaVencimiento(View view) {
        Calendar cal = Calendar.getInstance();
        int añio = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(Vencimientos.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month= month+1;
              String fecha = year + "-"+month +"-" + dayOfMonth;
              fechavenci.setText(fecha);
            }
        }, añio,mes,dia);
        dpd.show();
    }

    public void buscarvencimiento(View view) {
        recargartable();

        }



}