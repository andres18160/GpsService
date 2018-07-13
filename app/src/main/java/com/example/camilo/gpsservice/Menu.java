package com.example.camilo.gpsservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.camilo.gpsservice.Entidades.EnUsuario;

public class Menu extends AppCompatActivity {

    private EnUsuario usuario=new EnUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Bundle datos=getIntent().getExtras();
        usuario.setNombreDeUsuario(datos.getString("UserName"));

    }


}
