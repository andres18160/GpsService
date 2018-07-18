package com.example.camilo.gpsservice.Servicios;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.camilo.gpsservice.Activity.CoordenadasActivity;
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


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BackgroundJobService extends JobService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = BackgroundJobService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();

    public static final String ACTION_LOCATION_BROADCAST = BackgroundJobService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    private JobParameters JobParams;
    public static CoordenadasActivity UPDATE_LISTENER;
    private Handler handler;
    private  String longitud="0",latitud="0";

    public static void setUpdateListener(CoordenadasActivity activity) {
        UPDATE_LISTENER = activity;
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        JobParams=params;

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

       // jobFinished(params, false);
       // BootReceiver.scheduleJob(getApplicationContext());
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob");
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

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
            procesarCoordenadas(JobParams,String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

        }
    }
    private void sendMessageToUI(String lat, String lng) {

        Log.d(TAG, "Sending info...");

        procesarCoordenadas(JobParams,lat,lng);

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    private void procesarCoordenadas(final JobParameters params, final String longitud, final String latitud){
        //Toast.makeText(getApplicationContext(), "Coordenadas= "+coordenadas, Toast.LENGTH_SHORT).show();

        if(longitud=="" || latitud=="")
            return;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://10.10.41.75/php_service/Coordenadas.php";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG,"Response= "+ response);
                        jobFinished(params, false);
                        BootReceiver.scheduleJob(getApplicationContext());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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