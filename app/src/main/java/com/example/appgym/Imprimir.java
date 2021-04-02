package com.example.appgym;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.*;
import android.support.*;

import java.io.OutputStream;
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import com.itextpdf.text.Image;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static android.os.Environment.getDownloadCacheDirectory;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.example.appgym.TableDynamic.data;
import static com.example.appgym.TablaImprimir.data_im;
import static com.example.appgym.TableDynamic.tableLayout;

import static com.example.appgym.TablaImprimir.tableLayout_im;
public class Imprimir extends AppCompatActivity {
    double sum;
    double tpagar;
    double preci;
    double monto;
    double tpagar1;

    private Object[] headerVentas = {"Id_Entrenador", "NumeroVenta", "FechaVenta", "Monto"};
    private Object[] headerPlan = {"id_Entrenador","Id_Cliente","Id_Plan","FechaCompra","Monto"};
    private TextView fechavenci,txtTotal;
    public TableDynamic tableDynamic = new TableDynamic();
    public TablaImprimir tableImprimir = new TablaImprimir();
    private ArrayList<Object[]> rows = new ArrayList<>();
    private ArrayList<Object[]> rows1 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprimir);
        fechavenci = (TextView) findViewById(R.id.fecha_imprimir);
        txtTotal = (TextView)findViewById(R.id.txtTotal_Imprimir);
        TablaImprimir.tableLayout_im = findViewById(R.id.table_imprimir_ventas);
        TablaImprimir.context_im = getApplicationContext();
        TableDynamic.tableLayout = findViewById(R.id.table_imprimir_Plan);
        TableDynamic.context = getApplicationContext();
        fechavenci.setText(fechaActual());
        cargartable();
        tableImprimir.addHeader(headerVentas);
        tableImprimir.addData(getfecha_Ventas());
        tableImprimir.backgroundHeader(Color.parseColor("#FF9800"));
        tableImprimir.textColorHeader(Color.rgb(0, 0, 0));
        tableImprimir.textColorData(Color.parseColor("#FF9800"));
        calcularTotal();
        if(ActivityCompat.checkSelfPermission(Imprimir.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Imprimir.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Imprimir.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);

        }
    }
    public void ButtonAction()throws IOException, DocumentException {

        String fee=fechavenci.getText().toString();
        try{

            File file = crearFichero(fee+".pdf");

            FileOutputStream ficheroPDF = new FileOutputStream(file.getAbsolutePath());
           // OutputStream file = new FileOutputStream(new File("C:\\backupsistem\\Ventas\\"+fee+".pdf"));
            Document document = new Document();
            //Image Imagen= Image.getInstance("");

            PdfWriter.getInstance(document, ficheroPDF);


            document.open();
            PdfPTable tabla = new PdfPTable(4);
            Paragraph t = new Paragraph("\n\n", FontFactory.getFont("Arial",20,Font.ITALIC,BaseColor.RED));
            Paragraph p = new Paragraph("Ventas realizadas\n\n", FontFactory.getFont("Arial",16,Font.ITALIC,BaseColor.BLUE));
            Paragraph f = new Paragraph("FECHA: "+fee, FontFactory.getFont("Arial",20,Font.ITALIC,BaseColor.BLACK));
        //    Imagen.setAbsolutePosition(10f, 700f);
          //  document.add(Imagen);
            f.setAlignment(Element.ALIGN_RIGHT);

            document.add(f);
            t.setAlignment(Element.ALIGN_LEFT);
            document.add(t);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

        //    document.add(new Paragraph("AppGym/app/src/main/res/drawable/logonuevo.png"));



            float[] mediaCeldas={3.00f,3.50f,5.00f,4.00f};

            tabla.setWidths(mediaCeldas);
            tabla.addCell(new Paragraph("ID Entrenador", FontFactory.getFont("Arial",12)));
            tabla.addCell(new Paragraph("NumeroVenta", FontFactory.getFont("Arial",12)));
            tabla.addCell(new Paragraph("FechaVenta", FontFactory.getFont("Arial",12)));
            tabla.addCell(new Paragraph("Monto", FontFactory.getFont("Arial",12)));




            for (int i = 1; i < tableLayout_im.getChildCount(); i++){
                //preci=Double.parseDouble(Table.getValueAt(i, 4).toString());

                TextView td0 =(TextView) tableImprimir.getCell(i, 0);
                TextView td1 =(TextView) tableImprimir.getCell(i, 1);
                TextView td2 =(TextView) tableImprimir.getCell(i, 2);
                TextView td3 =(TextView) tableImprimir.getCell(i, 3);

                tabla.addCell(new Paragraph(td0.getText().toString(), FontFactory.getFont("Arial",10)));
                tabla.addCell(new Paragraph(td1.getText().toString(), FontFactory.getFont("Arial",10)));
                tabla.addCell(new Paragraph(td2.getText().toString(), FontFactory.getFont("Arial",10)));
                tabla.addCell(new Paragraph(td3.getText().toString(), FontFactory.getFont("Arial",10)));

            }


            document.add(tabla);


            PdfPTable tabla1 = new PdfPTable(5);
            Paragraph p1 = new Paragraph("Contratación de Planes\n\n", FontFactory.getFont("Arial",16,Font.ITALIC,BaseColor.BLUE));

            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);

            document.add(new Paragraph(""));

            float[] mediaCeldas1={5.00f,3.50f,5.00f,4.00f,4.00f};

            tabla1.setWidths(mediaCeldas1);

            tabla1.addCell(new Paragraph("ID Entrenador", FontFactory.getFont("Arial",12)));
            tabla1.addCell(new Paragraph("User", FontFactory.getFont("Arial",12)));
            tabla1.addCell(new Paragraph("ID Plan", FontFactory.getFont("Arial",12)));
            tabla1.addCell(new Paragraph("FechaVenta", FontFactory.getFont("Arial",12)));
            tabla1.addCell(new Paragraph("Monto", FontFactory.getFont("Arial",12)));

            for (int i = 1; i < tableLayout.getChildCount(); i++){
                //preci=Double.parseDouble(Table.getValueAt(i, 4).toString());

                TextView td0 =(TextView) tableDynamic.getCell(i, 0);
                TextView td1 =(TextView) tableDynamic.getCell(i, 1);
                TextView td2 =(TextView) tableDynamic.getCell(i, 2);
                TextView td3 =(TextView) tableDynamic.getCell(i, 3);
                TextView td4 =(TextView) tableDynamic.getCell(i, 4);
                tabla1.addCell(new Paragraph(td0.getText().toString(), FontFactory.getFont("Arial",10)));
                tabla1.addCell(new Paragraph(td1.getText().toString(), FontFactory.getFont("Arial",10)));
                tabla1.addCell(new Paragraph(td2.getText().toString(), FontFactory.getFont("Arial",10)));
                tabla1.addCell(new Paragraph(td3.getText().toString(), FontFactory.getFont("Arial",10)));
                tabla1.addCell(new Paragraph(td4.getText().toString(), FontFactory.getFont("Arial",10)));

            }
            document.add(tabla1);
            Paragraph p2 = new Paragraph(" \n", FontFactory.getFont("Arial",16,Font.ITALIC,BaseColor.BLUE));

            p2.setAlignment(Element.ALIGN_CENTER);
            document.add(p2);
            PdfPTable tabla2 = new PdfPTable(2);

            document.add(new Paragraph(""));

            float[] mediaCeldas2={17.50f,4.00f};

            tabla2.setWidths(mediaCeldas2);
            tabla2.addCell(new Paragraph("           Total de Ventas:", FontFactory.getFont("Arial",12)));
            tabla2.addCell(new Paragraph(txtTotal.getText().toString(), FontFactory.getFont("Arial",12)));
            document.add(tabla2);
            document.close();
            Toast.makeText(Imprimir.this, "Se a creado el PDF", Toast.LENGTH_SHORT).show();
            muestraPDF(file.getAbsolutePath(),Imprimir.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
public void muestraPDF(String archivo, Context context){
        File file = new File(archivo);
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.fromFile(file),"application/pdf");
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    try{
        context.startActivity(intent);
    } catch (ActivityNotFoundException e){

        Toast.makeText(context,""+e,Toast.LENGTH_LONG).show();
    }
}
    public File crearFichero(String nombreFichero) {
        File ruta = getRuta();

        File fichero = null;
        if(ruta != null) {
            fichero = new File(ruta, nombreFichero);

        }

        return fichero;
    }
    public File getRuta() {
        File ruta = null;

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            ruta = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "VentasPDF");
                        ruta.mkdirs();
        }
        return ruta;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void fechaImprimir(View view) {
        Calendar cal = Calendar.getInstance();
        int añio = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(Imprimir.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month= month+1;
                String fecha = year + "-"+month +"-" + dayOfMonth;
                fechavenci.setText(fecha);
            }
        }, añio,mes,dia);
        dpd.show();
    }
    public void fechaimprimir(View view) {
        try {
            ButtonAction();

        } catch (IOException ex) {
            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();

        } catch (DocumentException ex) {
            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();

        }
    }

    public void buscarimprimir(View view) {
        recargartable();
        calcularTotal();
    }

    public void cargartable(){
        tableDynamic.addHeader(headerPlan);
        tableDynamic.addData(getfecha_Plan());
        tableDynamic.backgroundHeader(Color.parseColor("#FF9800"));
        tableDynamic.textColorHeader(Color.rgb(0, 0, 0));
        tableDynamic.textColorData(Color.parseColor("#FF9800"));

    }
    public void recargartable(){
        tableLayout.removeViewsInLayout(1,data.size());

        data.clear();
        tableDynamic.addData(getfecha_Plan());
        tableDynamic.textColorData(Color.parseColor("#FF9800"));

        tableLayout_im.removeViewsInLayout(1,data_im.size());

        data_im.clear();
        tableImprimir.addData(getfecha_Ventas());
        tableImprimir.textColorData(Color.parseColor("#FF9800"));

    }

    public ArrayList<Object[]> getfecha_Plan() {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        Object[] registros = new Object[5];
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;
        String fee = fechavenci.getText().toString();
        int colu;
        try {
            ps = cn.prepareStatement("SELECT Id_Entrenador,Id_Cliente,Id_Plan,FechaCompra,Monto FROM ClientePlan WHERE FechaCompra=?");
            ps.setString(1, fee);
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
            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();
        }
        return rows;
    }
    public ArrayList<Object[]> getfecha_Ventas() {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;
        Object[] registros = new Object[4];
        String fee = fechavenci.getText().toString();
        int colu;
        try {
            ps = cn.prepareStatement("SELECT Id_Entrenador,NumeroVenta,FechaVenta,Monto FROM Ventas WHERE FechaVenta=?");
            ps.setString(1, fee);
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            colu = rsmd.getColumnCount();
            while (rs.next()) {
                Object[] fila = new Object[colu];
                for (int indice = 0; indice < colu; indice++) {
                    fila[indice] = rs.getString(indice + 1);

                }
                rows1.add((Object[]) fila);
            }

        } catch (SQLException ex) {
            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();
        }
        return rows1;
    }

    public static String fechaActual(){
        Date fecha = new Date();
        DateFormat formatofecha = new SimpleDateFormat("yyyy-MM-dd");
        return formatofecha.format(fecha);

    }
    void calcularTotal(){
        sum=0;
        tpagar=0;
        tpagar1=0;
        for (int i = 1; i < tableLayout.getChildCount(); i++){
            TextView tvprecio =(TextView) tableDynamic.getCell(i, 4);
            preci=Double.parseDouble(tvprecio.getText().toString());

            tpagar=tpagar+(preci);
        }
        for (int i = 1; i < tableLayout_im.getChildCount(); i++){
            TextView tvmonto =(TextView) tableImprimir.getCell(i, 3);
            monto=Double.parseDouble(tvmonto.getText().toString());

            tpagar1=tpagar1+(monto);
        }
        sum=tpagar+tpagar1;
        txtTotal.setText("$"+sum);
    }
}