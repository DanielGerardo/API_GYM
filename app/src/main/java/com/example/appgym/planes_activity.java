package com.example.appgym;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
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
public class planes_activity extends AppCompatActivity {
    private EditText et1,et2,et3,et4,et5;
    public TableDynamic tableDynamic = new TableDynamic();
    private Object[]header={"ID","Nombre","Dias","Valor","Observaciónes"};
    private ArrayList<Object[]> rows=new ArrayList<>();
    private ImageButton btn1,btn2,btn3,btn4,btn5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes_activity);
        et1 = (EditText)findViewById(R.id.txtNom_Plan);
        et2 = (EditText)findViewById(R.id.txtDias_Plan);
        et3 = (EditText)findViewById(R.id.txtCosto_Plan);
        et4 = (EditText)findViewById(R.id.txtObs_Plan);
        et5 = (EditText)findViewById(R.id.txtID_Plan);
        TableDynamic.tableLayout = (TableLayout)findViewById(R.id.table_Plan);
        TableDynamic.context = getApplicationContext();
        btn1 = (ImageButton)findViewById(R.id.btnEliminar_Plan);
        btn2 = (ImageButton)findViewById(R.id.btnGuardar_Plan);
        btn3 = (ImageButton)findViewById(R.id.btnEditar_Plan);
        btn4 = (ImageButton)findViewById(R.id.btnBuscar_Plan);
        btn5 = (ImageButton)findViewById(R.id.btnRecargar_Plan);
        btn3.setVisibility(View.INVISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        cargartable();

        btn5.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                Limpiar();
            }
        });
    }

    public TableLayout cargartable(){
        tableDynamic.addHeader(header);
        tableDynamic.addData(getPlan());
        tableDynamic.backgroundHeader(Color.rgb(0, 149, 255));
        tableDynamic.textColorHeader(Color.rgb(0, 0, 0));
        return TableDynamic.tableLayout;
    }
    public ArrayList<Object[]>getPlan() {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;
        int colu;

        try {
            ps = cn.prepareStatement("SELECT Id_Plan,Nombre,Dias,valor,observaciones FROM Planes");
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
            Toast.makeText(this,"Error Tabla Entrenador"+ex,Toast.LENGTH_SHORT).show();
        }
        return rows;
    }
    void Limpiar(){
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        if(et5.getText().length()<=0){
            btn4.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.INVISIBLE);
            btn1.setVisibility(View.INVISIBLE);
        }
        tableLayout.removeViewsInLayout(1,data.size());
        tableDynamic.addDatanull();
        tableLayout.removeViewsInLayout(1,data.size());
        data.clear();
        tableDynamic.addData(getPlan());
    }
    void LimpiarPlan(){
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");


    }
   public void BuscarPlan(View v) {


        et5.requestFocus();
        if(et1.getText().length()>=0||et2.getText().length()>=0||et3.getText().length()>=0||et4.getText().length()>=0){
            LimpiarPlan();
            et1.setVisibility(View.INVISIBLE);
            et2.setVisibility(View.INVISIBLE);
            et3.setVisibility(View.INVISIBLE);
            et4.setVisibility(View.INVISIBLE);
            if(et5.getText().length()<=0){
                Toast.makeText(this,"Debes ingresar el ID",Toast.LENGTH_LONG).show();

            }
            else{
                String ID = et5.getText().toString();
                Conexion cc = new Conexion();
                Connection cn = cc.Conexion();
                PreparedStatement ps;
                ResultSet rs;
                ResultSetMetaData rsmd;
                int colu;
                int id = Integer.parseInt(ID);

                try {
                    ps = cn.prepareStatement("SELECT Id_Plan,Nombre,Dias,valor,observaciones FROM Planes WHERE Id_Plan=?");
                    ps.setInt(1,id);
                    rs = ps.executeQuery();
                    rsmd = rs.getMetaData();
                    colu = rsmd.getColumnCount();
                    while (rs.next()) {
                        et1.setText(rs.getString("Nombre"));
                        et2.setText(rs.getString("Dias"));
                        et3.setText(rs.getString("valor"));
                        et4.setText(rs.getString("observaciones"));

                        et1.setVisibility(View.VISIBLE);
                        et2.setVisibility(View.VISIBLE);
                        et3.setVisibility(View.VISIBLE);
                        et4.setVisibility(View.VISIBLE);

                        if(et5.getText().length()>0){
                            btn3.setVisibility(View.VISIBLE);
                            btn1.setVisibility(View.VISIBLE);
                            btn4.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (SQLException ex) {
                    Toast.makeText(this, "Error Plan" + ex, Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    public void EditarPlan(View view) {
        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        String sql = "";

        sql = "UPDATE Planes SET Nombre=?, Dias=?, valor=?, observaciones=? WHERE Id_Plan=?";

        if (et1.length() == 0 || et2.length() <= 0 || et3.length() <= 0 || et4.length() == 0 || et5.length()<=0){
            Toast.makeText(this, "Debes de llenar los campos", Toast.LENGTH_LONG).show();
            et4.setText("null");
            if(et5.length()<=0){
                Toast.makeText(this,"Debes de Buscar el plan",Toast.LENGTH_LONG).show();
            }
        } else {
            String nom = et1.getText().toString().toUpperCase();
            int dias = Integer.parseInt(et2.getText().toString());
            Double costo = Double.parseDouble(et3.getText().toString());
            String obs = et4.getText().toString().toUpperCase();
            int id = Integer.parseInt(et5.getText().toString());
            try {
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, nom);
                pst.setInt(2, dias);
                pst.setDouble(3, costo);
                pst.setString(4, obs);
                pst.setInt(5, id);

                int n = pst.executeUpdate();
                if(n<=0){
                    Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                if (n > 0) {

                    Toast.makeText(this, "Registro Editado con exito!", Toast.LENGTH_SHORT).show();
                    Limpiar();
                }

            } catch (SQLException ex) {
                Logger.getLogger(NuevoEntrenador.class.getName()).log(Level.SEVERE, "null", ex);
            }
        }
    }
    public void GuardarPlan(View view) {
        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        String sql = "";

        sql = "insert into Planes (Id_Plan,Nombre,Dias,valor,observaciones) values (?,?,?,?,?)";

        if (et1.length() == 0 || et2.length() <= 0 || et3.length() <= 0 || et4.length() == 0){
            et4.setText("null");
            Toast.makeText(this, "Debes de llenar los campos", Toast.LENGTH_LONG).show();
            if(et5.length()<=0){
                Toast.makeText(this,"Debes de Buscar el plan",Toast.LENGTH_LONG).show();
            }
        } else {
            String nom = et1.getText().toString().toUpperCase();
            int dias = Integer.parseInt(et2.getText().toString());
            Double costo = Double.parseDouble(et3.getText().toString());
            String obs = et4.getText().toString().toUpperCase();
            int id = Integer.parseInt(et5.getText().toString());
            try {
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setInt(1, id);
                pst.setString(2, nom);
                pst.setInt(3, dias);
                pst.setDouble(4, costo);
                pst.setString(5, obs);

                int n = pst.executeUpdate();
                if(n<=0){
                    Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                if (n > 0) {
                    Limpiar();
                    Toast.makeText(this, "Registro Guardado con exito!", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException ex) {
                Logger.getLogger(NuevoEntrenador.class.getName()).log(Level.SEVERE, "null", ex);
            }
        }
    }
    public void EliminarPlan(View view) {
        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        String sql = "";
        sql = "DELETE Planes WHERE Id_Plan=?";
            if(et5.length()<=0){
                Toast.makeText(this,"Debes de Buscar el plan",Toast.LENGTH_LONG).show();
            } else {
            String nom = et1.getText().toString();
            int dias = Integer.parseInt(et2.getText().toString());
            Double costo = Double.parseDouble(et3.getText().toString());
            String obs = et4.getText().toString();
            int id = Integer.parseInt(et5.getText().toString());
            try {
                PreparedStatement pst = cn.prepareStatement(sql);

                pst.setInt(1, id);

                int n = pst.executeUpdate();
                if(n<=0){
                    Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                if (n > 0) {

                    Toast.makeText(this, "Registro Eliminado con exito!", Toast.LENGTH_SHORT).show();
                    Limpiar();
                }
            } catch (SQLException ex) {
                Toast.makeText(this, "El Plan esta en uso. Unicamnete se puede Modificado", Toast.LENGTH_SHORT).show();
                Logger.getLogger(NuevoEntrenador.class.getName()).log(Level.SEVERE, "null", ex);
            }
        }
    }
}