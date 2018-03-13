package com.elescalon.jdemu.elescalon;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jdemu on 12/02/2018.
 */

public class Participantes extends Fragment {
    View vista;
    ListView partici;
    TextView search;
    ImageView cancel;
    adaptadorParticipantes adaptadorParticipantes;
    Bundle b;
    String comuni;
    String correo;
    Usuario usuario;
    ArrayList<Usuario> usuaruios = new ArrayList();
    GridView gv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (vista != null) {
            ViewGroup parent = (ViewGroup) vista.getParent();
            parent.removeView(vista);
        } else {
            vista = inflater.inflate(R.layout.participantes, container, false);
            //vista = inflater.inflate(R.layout.foro, container, false);
            //partici = (ListView) vista.findViewById(R.id.lvForo);
            gv = (GridView) vista.findViewById(R.id.gri);
            search = (TextView) vista.findViewById(R.id.search2);
            cancel = (ImageView) vista.findViewById(R.id.cancel4);
            cancel.setEnabled(false);
            cancel.setVisibility(View.INVISIBLE);
            b = getArguments();
            comuni = b.getString("comuni");
            correo = b.getString("correo");
            b.remove("numPag");
            b.putInt("numPag",4);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search.setText("");
                    cancel.setEnabled(false);
                    cancel.setVisibility(View.INVISIBLE);
                }
            });
            partc();
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String c = "" + s;
                    if (!c.equals("")) {
                        ArrayList buscado = new ArrayList();
                        int longitud = s.length();
                        for (int i = 0; i < usuaruios.size(); i++) {
                            Usuario m = usuaruios.get(i);
                            int longitud2 = m.getNombre().length();
                            if (longitud2 >= longitud) {
                                String d = m.getNombre().substring(0, longitud).toLowerCase();
                                if (d.equals(c)) {
                                    buscado.add(m);
                                }
                            }
                        }
                        adaptadorParticipantes = new adaptadorParticipantes(getActivity(), buscado);
                        gv.setAdapter(adaptadorParticipantes);
                        cancel.setEnabled(true);
                        cancel.setVisibility(View.VISIBLE);
                    } else {
                        adaptadorParticipantes = new adaptadorParticipantes(getActivity(), usuaruios);
                        gv.setAdapter(adaptadorParticipantes);
                        cancel.setEnabled(false);
                        cancel.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Usuario user = (Usuario) gv.getItemAtPosition(i);
                    String Ucorreo=user.getCorreo();
                    Intent intent = new Intent(vista.getContext(), vecino.class);
                    intent.putExtra("Ucorreo", Ucorreo);
                    intent.putExtra("comunidadA",comuni);
                    intent.putExtra("micorreo",correo);
                    startActivity(intent);
                }
            });
        }
        return vista;
    }

    public void partc() {
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(comuni).child("Participantes");
        dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> emails = new ArrayList();
                final ArrayList<String> titulos = new ArrayList<>();
                final ArrayList<String> descriptions = new ArrayList<>();
                Iterator<DataSnapshot> hijos = dataSnapshot.getChildren().iterator();
                while (hijos.hasNext()) {
                    DataSnapshot dato = (DataSnapshot) hijos.next();
                    String h = dato.getKey();
                    emails.add(h);
                }
                for (int i = 0; i < emails.size(); i++) {
                    try {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        final StorageReference storageRef = storage.getReferenceFromUrl("gs://elescalon-79fa4.appspot.com").child("images").child(emails.get(i));
                        final File localFile = File.createTempFile("images", "jpg");
                        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                final Bitmap bit = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                 final String[] nom = new String[1];
                                final String email = storageRef.getName();
                                DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(email);
                                dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        nom[0] = dataSnapshot.child("nombre").getValue(String.class);
                                        usuario = new Usuario(nom[0], bit,email);
                                        usuaruios.add(usuario);
                                        adaptadorParticipantes = new adaptadorParticipantes(getActivity(), usuaruios);
                                        gv.setAdapter(adaptadorParticipantes);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });

                    } catch (IOException e) {

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}