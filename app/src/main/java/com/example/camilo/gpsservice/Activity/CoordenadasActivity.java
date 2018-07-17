package com.example.camilo.gpsservice.Activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.camilo.gpsservice.R;
import com.example.camilo.gpsservice.Servicios.BackgroundIntentService;
import com.example.camilo.gpsservice.Servicios.BackgroundJobService;
import com.example.camilo.gpsservice.Servicios.BootReceiver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CoordenadasActivity  extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mapa;
    private String Latitud="-75.5584314",Longitud="6.1962619";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordenadas);
        // Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            BackgroundIntentService.setUpdateListener(this);
        } else {
            BackgroundJobService.setUpdateListener(this);
        }

    }


    public void ActualizarCoordenadas(String latitud,String longitud) {
        Latitud=latitud;
        Longitud=longitud;
        if(mapa!=null){
            mapa.clear();
            LatLng yo = new LatLng(Double.parseDouble(Latitud),Double.parseDouble(Longitud));
            mapa.addMarker(new MarkerOptions().position(yo).title("YO"));
            //mapa.moveCamera(CameraUpdateFactory.newLatLng(yo));
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(Latitud),Double.parseDouble(Longitud)), 18.0f));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng yo = new LatLng(Double.parseDouble(Latitud),Double.parseDouble(Longitud));
        mapa.addMarker(new MarkerOptions().position(yo).title("YO"));
        mapa.moveCamera(CameraUpdateFactory.newLatLng(yo));



    }
}
