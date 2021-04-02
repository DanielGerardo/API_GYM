package com.example.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Control_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_);
    }
    public void vencimientos(View v){
        Intent ne=new Intent(Control_Activity.this,Vencimientos.class); startActivity(ne);
    }
    public void EntradasySalidas(View v){
        Intent ne=new Intent(Control_Activity.this,EntradasySalidas.class); startActivity(ne);
    }
     public void imprimir(View v){
        Intent ne=new Intent(Control_Activity.this,Imprimir.class); startActivity(ne);
    }
}