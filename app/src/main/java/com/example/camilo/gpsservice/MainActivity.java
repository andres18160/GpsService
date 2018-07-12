package com.example.camilo.gpsservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textoCronometro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoCronometro = findViewById(R.id.cronometro);

        Button startButton = findViewById(R.id.btn_iniciar);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                iniciarCronometro();
            }
        });

        Button stopButton = (Button) findViewById(R.id.btn_finalizar);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pararCronometro();
            }
        });

        SolicitarPermisosGPS();


    }

    @Override
    protected void onDestroy() {
        // Antes de cerrar la aplicacion se para el servicio (el cronometro)
        pararCronometro();
        super.onDestroy();
    }

    /**
     * Inicia el servicio
     */
    private void iniciarCronometro() {

    }

    /**
     * Finaliza el servicio
     */
    private void pararCronometro() {

    }

    /**
     * Actualiza en la interfaz de usuario el tiempo cronometrado
     *
     * @param tiempo
     */
    public void actualizarCronometro(double tiempo) {
        textoCronometro.setText(String.format("%.2f", tiempo) + "s");
    }


    private void SolicitarPermisosGPS() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }


        }

        int permissionCheckInfoPhone=ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        1);
            }


        }
    }
}