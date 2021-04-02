package com.example.appgym;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.appgym.TableDynamic.data;
import static com.example.appgym.TableDynamic.tableLayout;
import static com.example.appgym.TableDynamic.context;

public class Productos_Activity extends AppCompatActivity {
    private EditText et1, et2, et3, et4, et5, et6;
    private TableLayout tl1;
    private Object[] header = {"ID", "Descripción", "Unidad", "Stock", "Precio", "Fecha"};
    private Object[] header1 = {""};
    private ArrayList<Object[]> rows = new ArrayList<>();
    private ImageButton btn1, btn2, btn3, btn4, btn5;
    private Button btn6, btn7, btn8,btn9;
    private DialogInterface dl;
    private LinearLayout ly;
    public TableDynamic tableDynamic = new TableDynamic();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_);

        et1 = (EditText) findViewById(R.id.txtId_Pro);
        et2 = (EditText) findViewById(R.id.txtDes_Pro);
        et3 = (EditText) findViewById(R.id.txtUnidad_Pro);
        et4 = (EditText) findViewById(R.id.txtStock_Pro);
        et5 = (EditText) findViewById(R.id.txtPrecio_Pro);
        et6 = (EditText) findViewById(R.id.txtCantidad_Pro);
        TableDynamic.tableLayout = (TableLayout) findViewById(R.id.table_Pro);
        btn1 = (ImageButton) findViewById(R.id.btnEliminar_Pro);
        btn2 = (ImageButton) findViewById(R.id.btnGuardar_Pro);
        btn3 = (ImageButton) findViewById(R.id.btnEditar_Pro);
        btn4 = (ImageButton) findViewById(R.id.btnBuscar_Pro);
        btn5 = (ImageButton) findViewById(R.id.btnRecargar_Pro);
        btn6 = (Button) findViewById(R.id.btnHistorial);
        btn7 = (Button) findViewById(R.id.btnSalidas_Pro);
        btn8 = (Button) findViewById(R.id.btnEntradas_Pro);
        btn9 = (Button) findViewById(R.id.btnAgregar_Pro);
        ly = (LinearLayout) findViewById(R.id.ly_Pro);
        TableDynamic.context = getApplicationContext();
        btn9.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        et6.setVisibility(View.INVISIBLE);
        cargartable();

        btn5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Limpiar();
            }
        });
    }




    public TableLayout cargartable(){
        tableDynamic.addHeader(header);
        tableDynamic.addData(getProducto());
        tableDynamic.backgroundHeader(Color.rgb(0, 149, 255));
        tableDynamic.textColorHeader(Color.rgb(0, 0, 0));
        tableDynamic.cellred(3, Color.rgb(250, 100, 100));
        return TableDynamic.tableLayout;
    }



    public class time extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i=1;i<3;i++){
                hilo();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {


        }

    }

    public void hilo(){
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

   public void ejecutarBorrarTabla(){

    }

    public void ejecutar(){
        time time = new time();
        time.execute();
    }

    Conexion cc = new Conexion();
    Connection cn = cc.Conexion();

    void guardarHistorialS() {
        String fecha, id_producto, descripcion, unidad, cantidad;
        String sql = "";

        id_producto = et1.getText().toString();
        descripcion = et2.getText().toString().toUpperCase();
        ;
        unidad = et3.getText().toString().toUpperCase();
        ;
        cantidad = et6.getText().toString();

        sql = "INSERT INTO historial (movimiento, fecha, id_producto, descripcion, unidad, cantidad)"
                + "VALUES (?,GETDATE(),?,?,?,?)";
        try {
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, "SALIDA");
            p.setString(2, id_producto);
            p.setString(3, descripcion);
            p.setString(4, unidad);
            p.setString(5, cantidad);

            int n = p.executeUpdate();

            if (n > 0) {
            }

        } catch (SQLException ex) {
            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();
        }
    }

    void guardarHistorialE() {
        String fecha, id_producto, descripcion, unidad, cantidad;
        String sql = "";

        id_producto = et1.getText().toString();
        descripcion = et2.getText().toString().toUpperCase();
        ;
        unidad = et3.getText().toString().toUpperCase();
        ;
        cantidad = et6.getText().toString();

        sql = "INSERT INTO historial (movimiento, fecha, id_producto, descripcion, unidad, cantidad)"
                + "VALUES (?,GETDATE(),?,?,?,?)";
        try {
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, "ENTRADA");
            p.setString(2, id_producto);
            p.setString(3, descripcion);
            p.setString(4, unidad);
            p.setString(5, cantidad);

            int n = p.executeUpdate();

            if (n > 0) {
            }

        } catch (SQLException ex) {
            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();
        }
    }

    public void historial(View v) {
        Intent ne = new Intent(Productos_Activity.this, Historial_activity.class);
        startActivity(ne);
    }

    public ArrayList<Object[]> getProducto() {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;
        int colu;

        try {
            ps = cn.prepareStatement("SELECT Id_producto,Descripcion,Unidad,Cantidad,Precio,Fecha FROM Productos");
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

    public void Guardar_Pro(View v) {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        String sql = "";

        String ID = et1.getText().toString();
        String Des = et2.getText().toString().toUpperCase();
        String Uni = et3.getText().toString().toUpperCase();
        String Sto = et4.getText().toString();
        String Pre = et5.getText().toString();

        sql = "insert into Productos (id_producto,Descripcion,Unidad,Cantidad,Precio,Fecha) values (?,?,?,?,?,GETDATE())";

        if (et1.length() == 0 || et2.length() == 0 || et3.length() == 0 || et4.length() == 0 || et5.length() == 0) {
            Toast.makeText(this, "Desbes de llenar los campos", Toast.LENGTH_LONG).show();
        } else {
            try {

                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, ID);
                pst.setString(2, Des);
                pst.setString(3, Uni);
                pst.setString(4, Sto);
                pst.setString(5, Pre);


                int n = pst.executeUpdate();
                if(n<=0){
                    Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                if (n > 0) {
                    Toast.makeText(this, "Registro Guardado con exito!", Toast.LENGTH_LONG).show();
                    Limpiar();
                }

            } catch (SQLException ex) {
                Toast.makeText(this, "" + ex, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Edit_Pro(View v) {

        Toast.makeText(this, "Edita el stock en ENTRADAS/SALIDAS", Toast.LENGTH_LONG).show();
        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        String sql = "";

        String ID = et1.getText().toString();
        String Des = et2.getText().toString().toUpperCase();
        String Uni = et3.getText().toString().toUpperCase();

        String Pre = et5.getText().toString();

        sql = "UPDATE Productos SET Descripcion=?,Unidad=?,Precio=?,Fecha=GETDATE() WHERE id_producto=?";

        if (et1.length() == 0 || et2.length() == 0 || et3.length() == 0 || et4.length() == 0 || et5.length() == 0) {

            Toast.makeText(this, "Desbes de llenar los campos", Toast.LENGTH_LONG).show();

        } else {
            try {

                PreparedStatement pst = cn.prepareStatement(sql);

                pst.setString(1, Des);
                pst.setString(2, Uni);
                pst.setString(3, Pre);
                pst.setString(4, ID);

                int n = pst.executeUpdate();
                if(n<=0){
                    Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                if (n > 0) {
                    Toast.makeText(this, "Registro Modificado con exito!", Toast.LENGTH_LONG).show();
                    Limpiar();
                }

            } catch (SQLException ex) {
                Toast.makeText(this, "" + ex, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Eliminar_Pro(View v) {
        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        String sql = "";
        int ID = Integer.parseInt(et1.getText().toString());

        sql = "DELETE Productos WHERE id_producto=?";
        if (et1.length() == 0) {
            Toast.makeText(this, "El campo Id esta vació", Toast.LENGTH_LONG).show();
        } else {
            try {
                PreparedStatement pst = cn.prepareStatement(sql);

                pst.setInt(1, ID);

                int n = pst.executeUpdate();
                if(n<=0){
                    Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                if (n > 0) {
                    Toast.makeText(this, "El registro fué eliminado", Toast.LENGTH_SHORT).show();
                    Limpiar();
                }
            } catch (SQLException ex) {
                Toast.makeText(this, "El producto esta en uso. Unicamente se puede Modificar", Toast.LENGTH_SHORT).show();
                Logger.getLogger(NuevoEntrenador.class.getName()).log(Level.SEVERE, "null", ex);
            }
        }
    }

    void Limpiar() {
        Activa();
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        if (et1.getText().length()>= 0) {
            et1.setText("");
            btn4.setVisibility(View.VISIBLE);
        }
        btn3.setVisibility(View.INVISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        et5.setVisibility(View.VISIBLE);
        et2.setVisibility(View.VISIBLE);
        et3.setVisibility(View.VISIBLE);
        et4.setVisibility(View.VISIBLE);

        tableLayout.removeViewsInLayout(1,data.size());
        tableDynamic.addDatanull();
        tableLayout.removeViewsInLayout(1,data.size());
        data.clear();
        tableDynamic.addData(getProducto());
        tableDynamic.cellred(3, Color.rgb(250, 100, 100));
    }

    void LimpiarPro() {
        et5.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setVisibility(View.INVISIBLE);
        et2.setVisibility(View.INVISIBLE);
        et3.setVisibility(View.INVISIBLE);
        et4.setVisibility(View.INVISIBLE);
        btn7.setVisibility(View.INVISIBLE);
        btn8.setVisibility(View.INVISIBLE);
    }

    public void Buscar_Pro(View v) {
        et1.setEnabled(true);
        et1.requestFocus();
        if (et5.getText().length() >= 0 || et2.getText().length() >= 0 || et3.getText().length() >= 0 || et4.getText().length() >= 0) {
            LimpiarPro();

            if (et1.getText().length() <= 0) {
                Toast.makeText(this, "Debes ingresar el ID", Toast.LENGTH_LONG).show();

            } else {
                String ID = et1.getText().toString();
                Conexion cc = new Conexion();
                Connection cn = cc.Conexion();
                PreparedStatement ps;
                ResultSet rs;
                ResultSetMetaData rsmd;
                int colu;
                int id = Integer.parseInt(ID);

                try {
                    ps = cn.prepareStatement("SELECT Descripcion,Unidad,Cantidad,Precio FROM Productos WHERE id_producto=?");
                    ps.setInt(1, id);
                    rs = ps.executeQuery();
                    rsmd = rs.getMetaData();
                    colu = rsmd.getColumnCount();

                    while (rs.next()) {
                        et2.setText(rs.getString("Descripcion"));//Nom
                        et3.setText(rs.getString("Unidad"));
                        et4.setText(rs.getString("Cantidad"));
                        et5.setText(rs.getString("Precio"));

                        et5.setVisibility(View.VISIBLE);
                        et2.setVisibility(View.VISIBLE);
                        et3.setVisibility(View.VISIBLE);
                        et4.setVisibility(View.VISIBLE);


                        if (et1.getText().length() > 0) {
                            btn3.setVisibility(View.VISIBLE);
                            btn1.setVisibility(View.VISIBLE);
                            btn4.setVisibility(View.INVISIBLE);
                            btn7.setVisibility(View.VISIBLE);
                            btn8.setVisibility(View.VISIBLE);
                            if (btn9.getText() == "Sacar" || btn9.getText() == "Agregar") {
                                btn4.setVisibility(View.VISIBLE);
                                btn3.setVisibility(View.INVISIBLE);
                                btn1.setVisibility(View.INVISIBLE);
                                btn7.setVisibility(View.INVISIBLE);
                                btn8.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Toast.makeText(this, "Error Plan" + ex, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    void BorraeInactiva() {
        et1.setEnabled(false);
        et2.setEnabled(false);
        et3.setEnabled(false);
        et4.setEnabled(false);
        et5.setEnabled(false);
        et1.setTextColor(Color.parseColor("#FF636363"));
        et2.setTextColor(Color.parseColor("#FF636363"));
        et3.setTextColor(Color.parseColor("#FF636363"));
        et4.setTextColor(Color.parseColor("#FF636363"));
        et5.setTextColor(Color.parseColor("#FF636363"));
        btn2.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        btn7.setVisibility(View.INVISIBLE);
        btn8.setVisibility(View.INVISIBLE);
        et6.setVisibility(View.VISIBLE);
    }

    void Activa() {
        et1.setEnabled(true);
        et2.setEnabled(true);
        et3.setEnabled(true);
        et4.setEnabled(true);
        et5.setEnabled(true);
        et1.setTextColor(Color.parseColor("#03A9F4"));
        et2.setTextColor(Color.parseColor("#03A9F4"));
        et3.setTextColor(Color.parseColor("#03A9F4"));
        et4.setTextColor(Color.parseColor("#03A9F4"));
        et5.setTextColor(Color.parseColor("#03A9F4"));
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btn1.setVisibility(View.VISIBLE);
        btn7.setVisibility(View.VISIBLE);
        btn8.setVisibility(View.VISIBLE);
        btn9.setText("");
        btn9.setVisibility(View.INVISIBLE);
        et6.setVisibility(View.INVISIBLE);
    }

    public void btnEntradas(View v) {
        BorraeInactiva();
        btn9.setVisibility(View.VISIBLE);
        btn9.setBackgroundColor(Color.GREEN);
        btn9.setText("Agregar");
        btn9.setTextColor(Color.BLACK);
    }

    public void btnSalidas(View v) {
        BorraeInactiva();
        btn9.setBackgroundColor(Color.rgb(250,100,100));
        btn9.setVisibility(View.VISIBLE);
        btn9.setText("Sacar");
    }

    public void Cantidad(View v) {
            if(et4.getText().toString().equals("")){
                Toast.makeText(this,"Debes Seleccionar un Producto",Toast.LENGTH_LONG).show();
            }else{
                int can = Integer.parseInt(et6.getText().toString());
                int can1 = Integer.parseInt(et4.getText().toString());
                if(btn9.getText()=="Agregar"&&can>=1){
                    try {
                        PreparedStatement pst = cn.prepareStatement("UPDATE Productos SET Cantidad ="
                                + "Cantidad+'" + et6.getText().toString() + "'"
                                + "WHERE Id_Producto = '" + et1.getText().toString() + "'");

                            int a = pst.executeUpdate();
                            if (a > 0) {
                                Toast.makeText(this, "Se Agregaron "+et6.getText().toString()+" Piezas al Stock",Toast.LENGTH_LONG).show();
                                guardarHistorialE();
                                Limpiar();

                            } else {
                                Toast.makeText(this, "Selecciona un producto",Toast.LENGTH_LONG).show();
                            }
                    } catch (SQLException e) {
                        Toast.makeText(this, ""+e,Toast.LENGTH_LONG).show();
                    }
                }if(can<1){Toast.makeText(this, "Debes de ingresar valores mayores a 0",Toast.LENGTH_LONG).show();}
                if(btn9.getText()=="Sacar" &&can<=can1&&can>=1){
                    try {
                        PreparedStatement pst = cn.prepareStatement("UPDATE Productos SET Cantidad ="
                                + "Cantidad-'" + et6.getText().toString() + "'"
                                + "WHERE Id_Producto = '" + et1.getText().toString() + "'");
                            int a = pst.executeUpdate();
                            if (a > 0) {
                                Toast.makeText(this, "Sacaste "+et6.getText().toString()+" Piezas del Stock",Toast.LENGTH_LONG).show();
                                guardarHistorialS();
                                Limpiar();

                            } else {
                                Toast.makeText(this, "Selecciona un producto",Toast.LENGTH_LONG).show();
                            }

                    } catch (SQLException e) {
                        Toast.makeText(this, ""+e,Toast.LENGTH_LONG).show();
                    }
                }if(btn9.getText()=="Sacar"&&can>can1){
                    Toast.makeText(this, "No puedes quitar mas cantidad que la que tienes en el stock",Toast.LENGTH_LONG).show();
                }
            }

    }
}