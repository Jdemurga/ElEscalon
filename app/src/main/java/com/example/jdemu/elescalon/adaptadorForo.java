package com.example.jdemu.elescalon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jdemu on 18/01/2018.
 */

public class adaptadorForo extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Mensaje> items;

    public adaptadorForo(Activity activity, ArrayList<Mensaje> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemlista, null);
        }

        // Creamos un objeto directivo
        Mensaje dir = items.get(position);
        //Rellenamos la fotografía
        ImageView foto = (ImageView) v.findViewById(R.id.foto);
        foto.setImageDrawable(dir.getFoto());
        //Rellenamos el nombre
        TextView titulo = (TextView) v.findViewById(R.id.Titulo);
        titulo.setText(dir.getTitulo());
        //Rellenamos el cargo
        TextView msm = (TextView) v.findViewById(R.id.msm);
        msm.setText(dir.getMSM());

        // Retornamos la vista
        return v;
    }


}
