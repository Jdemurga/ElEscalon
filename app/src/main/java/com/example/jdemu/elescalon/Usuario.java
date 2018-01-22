package com.example.jdemu.elescalon;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by jdemu on 19/01/2018.
 */

public class Usuario {
    String nombre;
    String contraseña;
    int edad;
    String calle;
    String otros;
    String correo;
    public Usuario(String nombre, String contraseña, int edad,  String calle, String otros,String correo) {
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.edad = edad;
        this.calle = calle;
        this.otros = otros;
        this.correo=correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }



    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }


    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }
}
