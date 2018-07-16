package com.example.camilo.gpsservice.Entidades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLogin {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Response")
    @Expose
    private Response response;
    @SerializedName("State")
    @Expose
    private Boolean state;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

}

class Response {

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
