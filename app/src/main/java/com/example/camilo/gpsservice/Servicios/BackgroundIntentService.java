package com.example.camilo.gpsservice.Servicios;

import android.Manifest;
import android.app.IntentService;
import android.app.job.JobParameters;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.camilo.gpsservice.Activity.CoordenadasActivity;
import com.example.camilo.gpsservice.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class BackgroundIntentService extends IntentService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = BackgroundIntentService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    public static CoordenadasActivity UPDATE_LISTENER;
    private Handler handler;
    private  String longitud="0",latitud="0";


    public static void setUpdateListener(CoordenadasActivity activity) {
        UPDATE_LISTENER = activity;
    }

    public BackgroundIntentService() {
        super(BackgroundIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                UPDATE_LISTENER.ActualizarCoordenadas(latitud,longitud);
            }
        };
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

        Log.d(TAG, "Connected to Google API");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");

        if (location != null) {
            longitud=String.valueOf(location.getLongitude());
            latitud=String.valueOf(location.getLatitude());
            if(UPDATE_LISTENER != null) {
                handler.sendEmptyMessage(0);
            }
            procesarCoordenadas(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

        }
    }

    private void procesarCoordenadas(final String longitud, final String latitud){

        if(longitud=="" || latitud=="")
            return;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://10.10.41.75/php_service/Coordenadas.php";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("onResponse", response);
                        BootReceiver.scheduleIntent(getApplicationContext());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
                BootReceiver.scheduleIntent(getApplicationContext());
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String androidID =  Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(Calendar.getInstance().getTime());
                params.put("Longitud", longitud);
                params.put("Latitud", latitud);
                params.put("Mac", androidID);
                params.put("FechaHora", date);
                return params;
            }

        };

        requestQueue.add(jsonObjRequest);
    }
}