package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jdemu on 18/01/2018.
 */

public class foroMns extends Fragment {
    View vista;
    ListView foross;
    TextView search;
    ImageView cancel;
    adaptadorForo adaptadorForo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (vista != null) {
            ViewGroup parent = (ViewGroup) vista.getParent();
            parent.removeView(vista);
        } else {
            vista = inflater.inflate(R.layout.foro, container, false);
            foross = (ListView) vista.findViewById(R.id.lvForo);
            search = (TextView) vista.findViewById(R.id.search);
            cancel = (ImageView) vista.findViewById(R.id.cancel3);
            final ArrayList<Mensaje> mensajes = new ArrayList<>();
            Mensaje mns;
            for (int i = 0; i < 5; i++) {
                mns = new Mensaje(getResources().getDrawable(R.drawable.foto), "Mensaje " + i, "Esto es una prueba");
                mensajes.add(mns);
            }
            adaptadorForo = new adaptadorForo(this.getActivity(), mensajes);
            foross.setAdapter(adaptadorForo);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search.setText("");
                }
            });
            search.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String c= ""+ s;
                    if (!c.equals("")) {
                        ArrayList buscado = new ArrayList();
                        int longitud = s.length();
                        for (int i = 0; i < mensajes.size(); i++) {
                            Mensaje m = mensajes.get(i);
                            int longitud2=m.getTitulo().length();
                            if(longitud2>=longitud){
                                String d = m.getTitulo().substring(0, longitud).toLowerCase();
                                if (d.equals(c)) {
                                    buscado.add(m);
                                }
                            }
                        }
                        adaptadorForo = new adaptadorForo(getActivity(), buscado);
                        foross.setAdapter(adaptadorForo);

                    } else {
                        adaptadorForo = new adaptadorForo(getActivity(), mensajes);
                        foross.setAdapter(adaptadorForo);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
        return vista;
    }

}
