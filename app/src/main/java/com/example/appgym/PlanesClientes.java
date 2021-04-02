package com.example.appgym;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
import static java.time.temporal.ChronoUnit.DAYS;

public class PlanesClientes extends AppCompatActivity {
    public static String fechaActual(){
        Date fecha = new Date();
        DateFormat formatofecha = new SimpleDateFormat("yyyy-MM-dd");
        return formatofecha.format(fecha);

    }
    private EditText et1, et2, et3, et4, et5, et6;
    private TableLayout tl1;
    private Object[] header = {"Entrenador", "Cliente", "Plan", "Compra", "Vencimiento", "Monto"};
    private ArrayList<Object[]> rows = new ArrayList<>();
    private Button btn1;
    private Spinner spEn,spCl,spPn;
    public TableDynamic tableDynamic = new TableDynamic();
    String r;
    PreparedStatement ps;
    ResultSet rs;
    Conexion con = new Conexion();
    Connection acceso;
    EntidadEntrenador En = new EntidadEntrenador();
    ClientePlan v=new ClientePlan();
    Clientes cl=new Clientes();
    EnPlan eP=new EnPlan();
    ClientesDAO cdao=new ClientesDAO();
    Clientes cli=new Clientes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes_clientes);
        et1 = (EditText) findViewById(R.id.txtFechaCompra_PlanCl);
        et2 = (EditText) findViewById(R.id.txtFechaAnterior_PlanCl);
        et3 = (EditText) findViewById(R.id.txtVencimiento_PlanCl);
        et4 = (EditText) findViewById(R.id.txtDiasRestantes_PlanCl);
        et5 = (EditText) findViewById(R.id.txtNuevosDias_PlanCl);
        et6 = (EditText) findViewById(R.id.txtPrecio_PlanCl);
        spCl = (Spinner)findViewById(R.id.spnClientes_PlanCl);
        spEn = (Spinner)findViewById(R.id.spnEn_PlanCl);
        spPn = (Spinner)findViewById(R.id.spnPlan__PlanCl);
        TableDynamic.tableLayout = (TableLayout) findViewById(R.id.table_ClientePlan);
        btn1 = (Button) findViewById(R.id.btnComprar_Plan);

        TableDynamic.context = getApplicationContext();
        et1.setText(fechaActual());

        tableDynamic.addHeader(header);
        tableDynamic.backgroundHeader(Color.rgb(0, 149, 255));
        tableDynamic.textColorHeader(Color.rgb(0, 0, 0));
        tableDynamic.addData(PlanClientes());
        tableDynamic.fechadehoyVencimiento(4,Color.rgb(200,150,150),et1.getText().toString());
        spCl.setAdapter(mostrarCliente());
        spEn.setAdapter(mostrarEntrenador());
        spPn.setAdapter(mostrarPlan());
        spCl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0&&spPn.getSelectedItem().toString()!="Selecciona Plan"){
                int calcular;
                EnPlan estt = (EnPlan) spPn.getSelectedItem();
                Clientes est = (Clientes) spCl.getSelectedItem();
                et6.setText(""+ estt.getValor());
                et2.setText(""+ est.getFplan());
                if(et2.equals("")||et2.getText().equals("null")){
                    et2.setText(et1.getText());
                }
                String inicio = et1.getText().toString();
                String fin = et2.getText().toString();
                CalcularDias(inicio,fin);
                int dia= estt.getDias();
                int nuevo=Integer.parseInt(et4.getText().toString());
                calcular=nuevo+dia;
                et5.setText(String.valueOf(calcular));
                Date cal= Calendar(new Date(),calcular);
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                String date=df.format(cal);
                et3.setText(date);
                cargartable();
                tableDynamic.fechadehoyVencimiento(4,Color.rgb(200,150,150),et1.getText().toString());}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0&&spCl.getSelectedItem().toString()!="Selecciona Cliente"){
                int calcular;
                EnPlan est = (EnPlan) spPn.getSelectedItem();
                //txtDnuevo.setText("" + est.getDias());

                et6.setText("" + est.getValor());


                int dia= est.getDias();
                int nuevo = Integer.parseInt(et4.getText().toString());
                calcular=nuevo+dia;
                et5.setText(String.valueOf(calcular));
                Date cal= Calendar(new Date(),calcular);
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                String date=df.format(cal);
                et3.setText(date);
                String inicio = et1.getText().toString();

                String fin = et2.getText().toString();


                CalcularDias(inicio,fin);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onCreateDialog().show();
           }
       });



    }

void Limpiar(){
    tableLayout.removeViewsInLayout(1,data.size());
    tableDynamic.addDatanull();
    tableLayout.removeViewsInLayout(1,data.size());
    data.clear();
    tableDynamic.addData(PlanClientes());
    et2.setText("");
    et3.setText("");
    et4.setText("");
    et5.setText("");
    et6.setText("");
    spCl.setSelection(0);
    spEn.setSelection(0);
    spPn.setSelection(0);
    spPn.setEnabled(true);
    spCl.setEnabled(true);
    spEn.setEnabled(true);


}
    public TableLayout cargartable(){
        tableLayout.removeViewsInLayout(1,data.size());
        tableDynamic.addDatanull();
        tableLayout.removeViewsInLayout(1,data.size());
        data.clear();
        tableDynamic.addData(getPlanClientes());
        return TableDynamic.tableLayout;
    }

    int rr = 0;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public String CalcularDias(String inicio, String fin) {
        LocalDate currentTime = null;
        currentTime = LocalDate.parse(inicio);
        LocalDate my = null;
            my = LocalDate.parse(fin);
        long periodo = 0;
            periodo = DAYS.between(currentTime, my);

        if (periodo < 0) {
            periodo = 0;
            et4.setText(String.valueOf(periodo));
        } else {
            et4.setText(String.valueOf(periodo));
        }
        return r;
    }
    public Date Calendar(Date fecha,int dias){
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR,dias);
        return calendar.getTime();


    }

    public Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlanesClientes.this);


        LayoutInflater inflater = PlanesClientes.this.getLayoutInflater();


                builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                // Add action buttons
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        comprarPlan();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
   public void comprarPlan(){
    if(spCl.getSelectedItem().toString()=="Selecciona Cliente"||spEn.getSelectedItem().toString()=="Selecciona Entrenador"||spPn.getSelectedItem().toString()=="Selecciona Plan"){
        Toast.makeText(PlanesClientes.this,"Debés seleccionar todos los campos",Toast.LENGTH_LONG).show();
    }else{
        GuardarPlanC();
        guardarFecha();
        Limpiar();
    }
}


    public ArrayAdapter<EntidadEntrenador> mostrarEntrenador() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Conexion con = new Conexion();
        Connection acceso;

        ArrayAdapter<EntidadEntrenador> datos = new ArrayAdapter<EntidadEntrenador>(this,R.layout.spinner_item);
        EntidadEntrenador dat = null;


        try {
            acceso = con.Conexion();
            String sql = "SELECT * FROM Entrenador WHERE activo=1";
            ps = acceso.prepareStatement(sql);
            rs = ps.executeQuery();
            dat = new EntidadEntrenador();
            dat.setIdEn(0);
            dat.setNom("Selecciona Entrenador");
            datos.add(dat);

            while (rs.next()) {
                dat = new EntidadEntrenador();
                dat.setIdEn(rs.getInt("Id_Entrenador"));
                dat.setNom(rs.getString("Nombre"));
                datos.add(dat);

            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");


        }
        return datos;
    }
    public ArrayAdapter<Clientes> mostrarCliente() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Conexion con = new Conexion();
        Connection acceso;

        ArrayAdapter<Clientes> datosClientes = new ArrayAdapter<Clientes>(this, R.layout.spinner_item);
        Clientes dat = null;


        try {
            acceso = con.Conexion();
            String sql = "SELECT * FROM Clientes WHERE activo=1";
            ps = acceso.prepareStatement(sql);
            rs = ps.executeQuery();
            dat = new Clientes();
            dat.setId(0);
            dat.setNom("");
            dat.setUser("Selecciona Cliente");
            dat.setFplan("");
            datosClientes.add(dat);

            while (rs.next()) {
                dat = new Clientes();
                dat.setId(rs.getInt("Id_Cliente"));
                dat.setNom(rs.getString("Nombre"));
                dat.setUser(rs.getString("UserName"));
                dat.setFplan(rs.getString("fechaPlan"));
                datosClientes.add(dat);

            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");


        }
        return datosClientes;
    }
    public ArrayAdapter<EnPlan> mostrarPlan() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Conexion con = new Conexion();
        Connection acceso;

        ArrayAdapter<EnPlan> datos = new ArrayAdapter<EnPlan>(this, R.layout.spinner_item);
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
    public ArrayList<Object[]> getPlanClientes() {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;
        int colu;
        Clientes est = (Clientes) spCl.getSelectedItem();
        int id = Integer.parseInt(est.getUser());
        try {
            ps = cn.prepareStatement("SELECT id_Entrenador,Id_Cliente,Id_Plan,FechaCompra,FechaVencimiento,monto FROM ClientePlan WHERE Id_Cliente=?");
            ps.setInt(1,id);
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
            Toast.makeText(this, "Error Tabla Entrenador" + ex, Toast.LENGTH_SHORT).show();
        }
        return rows;
    }
    public ArrayList<Object[]> PlanClientes() {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;
        int colu;

        try {
            ps = cn.prepareStatement("SELECT id_Entrenador,Id_Cliente,Id_Plan,FechaCompra,FechaVencimiento,monto FROM ClientePlan");
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
            Toast.makeText(this, "Error Tabla Entrenador" + ex, Toast.LENGTH_SHORT).show();
        }
        return rows;
    }
    void GuardarPlanC(){
        EntidadEntrenador en = (EntidadEntrenador) spEn.getSelectedItem();
        Clientes est = (Clientes) spCl.getSelectedItem();
        EnPlan p = (EnPlan) spPn.getSelectedItem();
        int idE= en.getIdE();
        int idC= Integer.parseInt(est.getUser());
        int idP= p.getId();
        String fechac= et1.getText().toString();
        String fechaV= et3.getText().toString();
        double monto = Double.parseDouble(et6.getText().toString());
        v.setUser(idE);
        v.setIdC(idC);
        v.setIdP(idP);
        v.setFechaC(fechac);
        v.setFechaV(fechaV);
        v.setPrecio(monto);
        cdao.GuardarPlan(v);
    }
    void guardarFecha(){
        Clientes est = (Clientes) spCl.getSelectedItem();
        int idC= est.getId();
        String fechaV= et3.getText().toString();
        String sql = "UPDATE Clientes SET fechaPlan=? WHERE Id_Cliente=?";

        try {
            Conexion cc = new Conexion();
            Connection cn = cc.Conexion();
            PreparedStatement pst = cn.prepareStatement(sql);

            pst.setString(1, fechaV);
            pst.setInt(2, idC);



            int n = pst.executeUpdate();
            if (n > 0) {
                Toast.makeText(this, "Gracias, PONTE MAMADO!!",Toast.LENGTH_LONG).show();
            }

        } catch (SQLException ex) {

        }
    }
}