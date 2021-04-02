package com.example.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Menu_Clientes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__clientes);
    }

    public void Nuevocliente(View v){
        Intent ne=new Intent(Menu_Clientes.this,Clientes_Activity.class); startActivity(ne);
    }

    public void clientesplan(View v){
        Intent ne=new Intent(Menu_Clientes.this,PlanesClientes.class); startActivity(ne);
    }
}