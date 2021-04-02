package com.example.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class Respaldar_Activity extends AppCompatActivity {
    public  EditText et1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_respaldo);
        et1 = (EditText)findViewById(R.id.txtRespaldo_BD);

        String nom = et1.getText().toString();
        Intent m = new Intent(this, Menu.class);
        m.putExtra("nomBD",nom);

    }

}