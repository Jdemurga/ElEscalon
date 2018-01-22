package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by jdemu on 16/01/2018.
 */

public class Miscomunidades extends Fragment {
    View vista;
    ListView lv;
    ImageView borrar, buscar;
    EditText texto;
    ArrayAdapter<String> adapter;
    ArrayList<String> comunidades;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (vista != null) {
            ViewGroup parent = (ViewGroup) vista.getParent();
            parent.removeView(vista);
        } else {
            vista = inflater.inflate(R.layout.comunidad, container, false);
            lv = (ListView) vista.findViewById(R.id.Miscomunidades);
            borrar = (ImageView) vista.findViewById(R.id.cancel);
            buscar = (ImageView) vista.findViewById(R.id.lupa);
            texto = (EditText) vista.findViewById(R.id.txtBus);
           comunidades = new ArrayList();
            for (int i = 0; i < 8; i++) {
                int numero = i + 1;
                comunidades.add("Comunidad " + numero);
            }
            adapter = new ArrayAdapter(vista.getContext(), android.R.layout.simple_list_item_1, comunidades);
            lv.setAdapter(adapter);
            texto.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Miscomunidades.this.adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    texto.setText("");
                }
            });
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((inicio)getActivity()).entrarForo();
                }
            });


        }

        return vista;
    }
    public void añadirmios(String nuevo,String calle){
        comunidades.add(nuevo);
    }
}
