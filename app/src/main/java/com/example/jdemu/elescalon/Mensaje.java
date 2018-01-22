package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.graphics.drawable.Drawable;

/**
 * Created by jdemu on 18/01/2018.
 */

public class Mensaje {
    protected Drawable foto;
    protected String titulo;
    protected String msm;
    protected long id;

    public Mensaje(Drawable foto, String titulo, String msm) {
        super();
        this.foto = foto;
        this.titulo = titulo;
        this.msm = msm;
    }



    public Drawable getFoto() {
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
