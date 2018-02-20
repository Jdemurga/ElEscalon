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
 * Created by jdemu on 19/02/2018.
 */

public class adaptadorListaChat extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Usuario> items;

    public adaptadorListaChat(Activity activity, ArrayList<Usuario> items) {
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
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemlistachat, null);
        }

        // Creamos un objeto directivo
        Usuario dir = items.get(position);
        //Rellenamos la fotografía
        ImageView foto = (ImageView) v.findViewById(R.id.foto4);
        foto.setImageBitmap(dir.getFoto());
        //Rellenamos el nombre
        TextView name = (TextView) v.findViewById(R.id.nombreChat);
        name.setText(dir.getNombre());
        //Rellenamos el cargo


        // Retornamos la vista
        return v;
    }

}
