package com.elescalon.esj.elescalon;

import android.graphics.Bitmap;

/**
 * Created by esj on 18/01/2018.
 */

public class Mensaje {
    protected Bitmap foto;
    protected String titulo;
    protected String msm;
    protected long id;
    protected long fecha;

    protected String correo;


    public Mensaje(Bitmap foto, String titulo, String msm, String correo, long fecha) {
        super();
        this.foto = foto;
        this.titulo = titulo;
        this.msm = msm;
        this.correo = correo;
        this.fecha = fecha;

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

    public long getFecha() {
        return fecha;
    }

}
