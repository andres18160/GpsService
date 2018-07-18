package com.example.camilo.gpsservice.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.camilo.gpsservice.R;
import com.example.camilo.gpsservice.Servicios.BackgroundIntentService;
import com.example.camilo.gpsservice.Servicios.BackgroundJobService;
import com.example.camilo.gpsservice.Servicios.BootReceiver;

public class AdminServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service);
        ValidarInicioServicio();


    }


    private void ValidarInicioServicio(){
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if(isMyServiceRunning(BackgroundIntentService.class)){
                MensajeToast("Servicio Intent Corriendo");
            }else{
                MensajeToast("Servicio Intent Detenido");
            }
        } else {
            if(isMyServiceRunning(BackgroundJobService.class)){
                MensajeToast("Servicio Job Corriendo");
            }else{
                MensajeToast("Servicio Job Detenido");
            }
        }
    }

    public void IniciarServicio(View v){
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent();
            intent.setAction("com.example.camilo.gpsservice.Servicios.BootReceiver");
            sendBroadcast(intent);
        } else {
            BootReceiver.scheduleJob(getApplicationContext());
            //BootReceiver.scheduleIntentBeacon(getApplicationContext());
        }

        ValidarInicioServicio();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    public void MensajeToast(String mensaje){
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
