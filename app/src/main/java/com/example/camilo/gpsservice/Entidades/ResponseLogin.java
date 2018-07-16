package com.example.camilo.gpsservice.Entidades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLogin {

    @SerializedName("Base")
    @Expose
    private String base;
    @SerializedName("Cedula")
    @Expose
    private String cedula;
    @SerializedName("Nombre")
    @Expose
    private String nombre;
    @SerializedName("PersonaId")
    @Expose
    private Integer personaId;
    @SerializedName("Token")
    @Expose
    private String token;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Integer personaId) {
        this.personaId = personaId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}

