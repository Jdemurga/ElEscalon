package com.example.jdemu.elescalon;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    String correo;
    Bundle b;
    String comu;

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
            borrar.setEnabled(false);
            borrar.setVisibility(View.INVISIBLE);
            buscar = (ImageView) vista.findViewById(R.id.lupa);
            b = getArguments();
            correo = b.getString("correo");
            texto = (EditText) vista.findViewById(R.id.txtBus);
            comunidades = new ArrayList();
            leerListas();
            texto.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Miscomunidades.this.adapter.getFilter().filter(s);
                    if(!s.equals("")){
                        borrar.setEnabled(true);
                        borrar.setVisibility(View.VISIBLE);
                    }else{
                        borrar.setEnabled(false);
                        borrar.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    texto.setText("");
                    borrar.setEnabled(false);
                    borrar.setVisibility(View.INVISIBLE);
                }
            });
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    comu = String.valueOf(lv.getItemAtPosition(position));
                    ((inicio) getActivity()).entrarForo(comu, correo);
                }
            });
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    BorrarDialogo(String.valueOf(lv.getItemAtPosition(i)));
                    return true;
                }
            });


        }

        return vista;
    }

    public void leerListas() {
        final String[] nombres = new String[1];

        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);

        dbrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comunidades.clear();
                nombres[0] = dataSnapshot.child("comunidades").getValue(String.class);
                String[] com = nombres[0].split(",");
                for (int i = 0; i < com.length; i++) {
                    comunidades.add(com[i]);
                }
                adapter = new ArrayAdapter(vista.getContext(), android.R.layout.simple_list_item_1, comunidades);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void BorrarListas(String borr) {
        final String[] nombres = new String[1];
        final String borrar = borr;
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombres[0] = dataSnapshot.child("comunidades").getValue(String.class);
                String[] largo = nombres[0].split(",");
                DatabaseReference dbf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
                String dato = "";
                for (int i = 0; i < largo.length; i++) {
                    if (!largo[i].equals(borrar)) {
                        if (!dato.equals("")) {
                            dato += "," + largo[i];
                        } else {
                            dato += largo[i];
                        }
                    }
                }
                dbf.child("comunidades").setValue(dato);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference drf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(borrar).child("Foro");
        drf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("comunidades").child(borrar).child("Foro").child(correo);
                df.removeValue();
                DatabaseReference daf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(borrar).child("Participantes").child(correo);
                daf.removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void BorrarDialogo(final String comunidad) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(vista.getResources().getString(R.string.Borrarc))
                .setMessage(vista.getResources().getString(R.string.Borrarcm) + comunidad + "?")
                .setPositiveButton(vista.getResources().getString(R.string.acep),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BorrarListas(comunidad);
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
