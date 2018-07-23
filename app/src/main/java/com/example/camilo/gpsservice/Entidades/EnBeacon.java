package com.example.camilo.gpsservice.Entidades;

import android.support.annotation.NonNull;

public class EnBeacon implements Comparable<EnBeacon>{
    private String Id;
    private double distancia;



    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }


    @Override
    public int compareTo(@NonNull EnBeacon o) {
        if(distancia < o.distancia) {
            return -1;
        }
        if (distancia > o.distancia){
            return 1;
        }

        return 0;
    }
}
