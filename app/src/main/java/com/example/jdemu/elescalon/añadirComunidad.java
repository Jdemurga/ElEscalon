package com.example.jdemu.elescalon;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

public class añadirComunidad extends Fragment {
    View vista;
    ListView lv;
    FloatingActionButton fab;
    EditText txt;
    ImageView cancel;
    ArrayAdapter<String> adapter;
    ArrayList<String> comunidades;
    Miscomunidades mc= new Miscomunidades();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (vista != null) {
            ViewGroup parent = (ViewGroup) vista.getParent();
            parent.removeView(vista);
        } else {
            vista = inflater.inflate(R.layout.anadircomunidad, container, false);
            lv = (ListView) vista.findViewById(R.id.ComunidadesCre);
            fab = (FloatingActionButton) vista.findViewById(R.id.fab);
            txt = (EditText) vista.findViewById(R.id.txtBus2);
            cancel = (ImageView) vista.findViewById(R.id.cancel2);
            comunidades = new ArrayList();
            for (int i = 0; i < 15; i++) {
                int numero = i + 1;
                comunidades.add("Comunidad " + numero);
                comunidades.add("Vecindario " + numero);
            }
            adapter = new ArrayAdapter(vista.getContext(), android.R.layout.simple_list_item_1, comunidades);
            lv.setAdapter(adapter);
            txt.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    añadirComunidad.this.adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txt.setText("");
                }
            });
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String nombre = String.valueOf(lv.getItemAtPosition(position));

                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChangeLangDialog();
                }
            });
        }
        return vista;
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(vista.getContext());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        final EditText edt2 = (EditText) dialogView.findViewById(R.id.edit2);


        dialogBuilder.setTitle("Nuevo contacto");
        dialogBuilder.setMessage("Introduzca los datos");
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!(String.valueOf(edt.getText()).equals("") || String.valueOf(edt2.getText()).equals(""))) {
// usar para la base de datos                    mc.añadirmios(String.valueOf(edt.getText()),String.valueOf(edt2.getText()));
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
