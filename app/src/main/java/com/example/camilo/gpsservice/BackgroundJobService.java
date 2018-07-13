package com.example.camilo.gpsservice;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.System;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BackgroundJobService extends JobService {

    public String coordenadas="";
    public String latitud="";
    public String longitud="";
   private LocationManager locationManager;
   private LocationListener locationListener;
    private Timer temporizador = new Timer();
    private static final long INTERVALO_ACTUALIZACION = 5000; // En ms

    private  Context contexto=this;

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d("Coordenada","Servicio INICIADO");
        IniciarGps();
                temporizador.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        procesarCoordenadas(params);
                      //  jobFinished(params, false);
                    }
                }, 0, INTERVALO_ACTUALIZACION);
       // BootReceiver.scheduleJob(getApplicationContext());
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        locationManager.removeUpdates(locationListener);
        Log.i("Coordenada","Servicio Saliendo");
        jobFinished(jobParameters, false);
        BootReceiver.scheduleJob(getApplicationContext());
        return false;
    }

    public void IniciarGps(){

        locationManager =(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                coordenadas=location.getLatitude()+" "+location.getLongitude();
                Log.d("Coordenadas detectadas",coordenadas);
                latitud=String.valueOf(location.getLatitude());
                longitud=String.valueOf(location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }

    private void procesarCoordenadas(final JobParameters params){
        //Toast.makeText(getApplicationContext(), "Coordenadas= "+coordenadas, Toast.LENGTH_SHORT).show();

        if(longitud=="" || latitud=="")
            return;

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://192.168.1.123/php_service/Coordenadas.php";
            StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("onResponse", response);
                            jobFinished(params, false);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                   // Log.e("onErrorResponse", error.getMessage());
                    jobFinished(params, false);
                    BootReceiver.scheduleJob(getApplicationContext());
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String androidID = System.getString(contexto.getContentResolver(), Settings.Secure.ANDROID_ID);
                    params.put("Longitud", longitud);
                    params.put("Latitud", latitud);
                    params.put("Mac", androidID);
                    params.put("FechaHora", "2018-07-12 02:07:00");
                    return params;
                }

            };

            requestQueue.add(jsonObjRequest);

        Log.d("Coordenada",coordenadas);
    }



}