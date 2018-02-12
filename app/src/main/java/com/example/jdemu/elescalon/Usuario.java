package com.example.jdemu.elescalon;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by jdemu on 19/01/2018.
 */

public class Usuario {
    String nombre;
    Bitmap foto;

    public Usuario(String nombre, Bitmap foto) {
        this.nombre = nombre;
        this.foto = foto;

    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getFoto() {
        return foto;
    }
    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

}
