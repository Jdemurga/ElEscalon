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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

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
    Bundle b;
    String correo;

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
            b = getArguments();
            correo = b.getString("correo");
            comunidades = new ArrayList();
            leerTodasC();
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
                    añadirDialogo(nombre);

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


        dialogBuilder.setTitle("Nuevo comunidad");
        dialogBuilder.setMessage("Introduzca los datos");
        dialogBuilder.setPositiveButton(vista.getResources().getString(R.string.acep), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!(String.valueOf(edt.getText()).equals(""))) {
                    subirCom(String.valueOf(edt.getText()), String.valueOf(edt.getText()));
                }
            }
        });
        dialogBuilder.setNegativeButton(vista.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void leerTodasC() {


        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("comunidades");

        dbrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comunidades.clear();
                Iterator<DataSnapshot> hijos = dataSnapshot.getChildren().iterator();
                while (hijos.hasNext()) {
                    DataSnapshot dato = (DataSnapshot) hijos.next();
                    String h = dato.getKey();

                    comunidades.add(h);
                }
                adapter = new ArrayAdapter(vista.getContext(), android.R.layout.simple_list_item_1, comunidades);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void subirCom(String names, String misListas) {
        final String[] nombres = new String[1];
        final String nueva = misListas;
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombres[0] = dataSnapshot.child("comunidades").getValue(String.class);
                DatabaseReference dbf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
                if (!nombres[0].equals("")) {
                    nombres[0] += ",";
                }
                dbf.child("comunidades").setValue(nombres[0] + nueva);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("comunidades");
        df.child(names).push();
        df.child(names).child("Foro").push();
        df.child(names).child("Participantes").push();
        df.child(names).child("Participantes").child(correo).push();
        df.child(names).child("Participantes").child(correo).child("valoracion").push();
        df.child(names).child("Participantes").child(correo).child("numValor").push();
        df.child(names).child("Participantes").child(correo).child("valoracion").setValue(0);
        df.child(names).child("Participantes").child(correo).child("numValor").setValue(0);
    }

    public void añadirAMIs(String name) {
        final String[] nombres = new String[1];
        final String nueva = name;
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombres[0] = dataSnapshot.child("comunidades").getValue(String.class);
                DatabaseReference dbf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
                if (!nombres[0].equals("")) {
                    nombres[0] += ",";
                }
                dbf.child("comunidades").setValue(nombres[0] + nueva);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference drf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(nueva).child("Participantes");
        drf.child(correo).push();
        drf.child(correo).child("valoracion").push();
        drf.child(correo).child("numValor").push();
        drf.child(correo).child("valoracion").setValue(0);
        drf.child(correo).child("numValor").setValue(0);
    }

    public void añadirDialogo(final String comunidad) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Añadir comunidad")
                .setMessage("¿Desea añadir la comunidad "+comunidad+"?")
                .setPositiveButton(vista.getResources().getString(R.string.acep),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                añadirAMIs(comunidad);
                            }
                        })
                .setNegativeButton(vista.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        builder.create().show();
    }

}
