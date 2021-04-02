package com.example.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.VerifiedKeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    private EditText et1, et2;
   public EditText et3;
    public TextView tv1;
    private Button bn;
    public ImageView im;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //icono ponerlo en el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        et1 = (EditText) findViewById(R.id.txtUser);
        et2 = (EditText) findViewById(R.id.txtPass);

        bn = (Button) findViewById(R.id.btnIngresar);
        tv1 = (TextView)findViewById(R.id.txtNom);



       bn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Ingresar();

            }
        });
    }


    public void Ingresar() {

        Conexion cc = new Conexion();
        Connection cn = cc.Conexion();
        PreparedStatement ps;
        ResultSet rs;

        String User = et1.getText().toString();
        String Pass = et2.getText().toString();
        if (User.length() == 0 || Pass.length()== 0) {
            Toast.makeText(this, "Debe de ingresar datos", Toast.LENGTH_SHORT).show();
        }
        if (User.equals("danielcampos") && Pass.equals("123456")) {
            Intent m = new Intent(this, Menu.class);
            m.putExtra("admin","Ing.Campos");
            startActivity(m);
         

        } else {
            try {

                ps = cn.prepareStatement("SELECT * FROM Entrenador WHERE UserName=? AND Pass=? AND activo=1");
                ps.setString(1, User);
                ps.setString(2, Pass);
                rs = ps.executeQuery();


                if (rs.next()) {

                    Intent m = new Intent(this, Menu.class);
                    m.putExtra("Nombre",rs.getString("Nombre"));
                    startActivity(m);

                } else {
                       Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
                    et1.setText("");
                    et2.setText("");

                }

            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                 Toast.makeText(this, "Error de conexion" + e, Toast.LENGTH_SHORT).show();

            }
        }
    }
}