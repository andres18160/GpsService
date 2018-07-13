package com.example.camilo.gpsservice.Entidades;

import java.io.Serializable;

public class EnUsuario  implements Serializable{

    private int Id;
    private String NombreDeUsuario;
    private String Clave;
    private String Nombres;
    private String Base;
    private String Token;
    private String CodeUser;
    private String Estado;



    public int get_id() {
        return Id;
    }

    public void set_id(int _id) {
        this.Id = _id;
    }

    public String getNombreDeUsuario() {
        return NombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        NombreDeUsuario = nombreDeUsuario;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }


    public String getBase() {
        return Base;
    }

    public void setBase(String base) {
        Base = base;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getCodeUser() {
        return CodeUser;
    }

    public void setCodeUser(String codeUser) {
        CodeUser = codeUser;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
}

