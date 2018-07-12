package com.example.camilo.gpsservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class GpsService extends Service {
    public GpsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    private Timer temporizador = new Timer();
    private static final long INTERVALO_ACTUALIZACION = 10; // En ms
    public static MainActivity UPDATE_LISTENER;
    private double cronometro = 0;
    private Handler handler;

    /**
     * Establece quien va ha recibir las actualizaciones del cronometro
     *
     * @param contexto
     */
    public static void setUpdateListener(MainActivity contexto) {
        UPDATE_LISTENER = contexto;
    }


    @Override
    public int onStartCommand(Intent intent,int flag,int idProcess){
        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                UPDATE_LISTENER.actualizarCronometro(cronometro);
            }
        };

        iniciarServicio();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void onDestroy() {
        pararServicio();
        super.onDestroy();
    }



    private void iniciarServicio() {

        temporizador = new Timer();
        temporizador.schedule(new TimerTask() {
            @Override
            public void run() {
                cronometro += 0.01;
                if(UPDATE_LISTENER != null) {
                    handler.sendEmptyMessage(0);
                }
                Log.d("Contador",""+cronometro);
            }

        }, 0, INTERVALO_ACTUALIZACION);
    }

    private void pararServicio() {
        if (temporizador != null)
            temporizador.cancel();
    }

}