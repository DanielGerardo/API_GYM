package com.example.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu extends AppCompatActivity {

public ListView lv;
public TextView tv1;
Bundle datos;
//vector

private String botones[] = {"Nuevo Entrenador","Planes","Productos","Clientes","Ventas","Control","Respaldar"};
public EditText et1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        datos = getIntent().getExtras();

        String admin = datos.getString("admin");
        String Nombre = datos.getString("Nombre");

        tv1=(TextView)findViewById(R.id.txtNom);
        lv=(ListView)findViewById(R.id.listview);
        tv1.setText(Nombre);
       if(tv1.getText().length()==0){tv1.setText(admin);}

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, R.layout.listview_menu,botones);
        lv.setAdapter(adapter);

       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                  switch (i){
                      case 0: Intent ne=new Intent(Menu.this,NuevoEntrenador.class); startActivity(ne);break;
                      case 1: Intent pl=new Intent(Menu.this,planes_activity.class); startActivity(pl);break;
                      case 2: Intent pr=new Intent(Menu.this,Productos_Activity.class); startActivity(pr);break;
                      case 3: Intent cl=new Intent(Menu.this,Menu_Clientes.class); startActivity(cl);break;
                      case 4: Intent ve=new Intent(Menu.this,Ventas_Activity.class); startActivity(ve);break;
                      case 5: Intent co=new Intent(Menu.this,Control_Activity.class); startActivity(co);break;
                      case 6: onCreateDialog().show(); /*Intent re=new Intent(Menu.this,Respaldar_Activity.class); startActivity(re);*/ break;

                  }

           }
       });


    }

    public Dialog onCreateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);

        LayoutInflater inflater = Menu.this.getLayoutInflater();


   View view = inflater.inflate(R.layout.dialog_respaldo, null);
        final EditText edit = (EditText) view.findViewById(R.id.txtRespaldo_BD);
        builder.setView(view)

        // Add action buttons
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                      //  String nom = datos.getString("nomBD") oooO0Oooo


                        try {
                            Conexion cc = new Conexion();
                            PreparedStatement ps;
                            ResultSet rs;
                            Connection cn = cc.Conexion();
                            String nom = edit.getText().toString();
                            ps = cn.prepareStatement("BACKUP Database GYM TO DISK="+"'C:\\backupsistem\\"+nom+".bak'");
                            ps.executeUpdate();
                            Toast.makeText(Menu.this,"Se creo el respaldo: "+nom+"!",Toast.LENGTH_LONG).show();
                        } catch (SQLException ex) {
                            System.out.println(ex+"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();

    }


}