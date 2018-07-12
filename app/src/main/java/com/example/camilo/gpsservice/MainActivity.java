package com.example.camilo.gpsservice;

import android.content.Intent;
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

        GpsService.setUpdateListener(this);

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
        Intent service = new Intent(this, GpsService.class);
        startService(service);
    }

    /**
     * Finaliza el servicio
     */
    private void pararCronometro() {
        Intent service = new Intent(this, GpsService.class);
        stopService(service);
    }

    /**
     * Actualiza en la interfaz de usuario el tiempo cronometrado
     *
     * @param tiempo
     */
    public void actualizarCronometro(double tiempo) {
        textoCronometro.setText(String.format("%.2f", tiempo) + "s");
    }
}