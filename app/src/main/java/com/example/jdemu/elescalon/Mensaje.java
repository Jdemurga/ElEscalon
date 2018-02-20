package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by jdemu on 18/01/2018.
 */

public class Mensaje {
    protected Bitmap foto;
    protected String titulo;
    protected String msm;
    protected long id;
    protected  String correo;
    public Mensaje(Bitmap foto, String titulo, String msm,String correo) {
        super();
        this.foto = foto;
        this.titulo = titulo;
        this.msm = msm;
        this.correo=correo;
    }


    public String getCorreo() {
        return correo;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public String getTitulo() {
        return titulo;
    }


    public String getMSM() {
        return msm;
    }

    public long getId() {
        return id;
    }


}
