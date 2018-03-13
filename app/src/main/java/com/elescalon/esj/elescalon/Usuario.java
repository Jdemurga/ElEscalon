package com.elescalon.esj.elescalon;

import android.graphics.Bitmap;

/**
 * Created by esj on 19/01/2018.
 */

public class Usuario {
    String nombre;
    Bitmap foto;
    String correo;
    protected long id;

    public Usuario(String nombre, Bitmap foto,String correo) {
        this.nombre = nombre;
        this.foto = foto;
        this.correo = correo;

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

    public String getCorreo() {
        return correo;
    }

    public long getId() {
        return id;
    }

}
