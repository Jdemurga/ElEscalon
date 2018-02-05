package com.example.jdemu.elescalon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
 * Created by jdemu on 18/01/2018.
 */

public class foroMns extends Fragment {
    View vista;
    ListView foross;
    TextView search;
    ImageView cancel;
    FloatingActionButton fab;
    adaptadorForo adaptadorForo;
    Bundle b;
    String comuni;
    String correo;
    ArrayList<Mensaje> mensajes = new ArrayList<>();
    Mensaje mns;
    ArrayList<String> emails = new ArrayList();
    ArrayList<Bitmap> fotos = new ArrayList();
    ArrayList<String> titulos = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();

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
            fab = (FloatingActionButton) vista.findViewById(R.id.faMensaje);
            b = getArguments();
            comuni = b.getString("comuni");
            correo = b.getString("correo");
            llaves();
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
                    String c = "" + s;
                    if (!c.equals("")) {
                        ArrayList buscado = new ArrayList();
                        int longitud = s.length();
                        for (int i = 0; i < mensajes.size(); i++) {
                            Mensaje m = mensajes.get(i);
                            int longitud2 = m.getTitulo().length();
                            if (longitud2 >= longitud) {
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
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showChangeLangDialog();
                }
            });
        }
        return vista;
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(vista.getContext());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.mensajedialogo, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit3);
        final EditText edt2 = (EditText) dialogView.findViewById(R.id.edit4);


        dialogBuilder.setTitle("Nuevo mensaje");
        dialogBuilder.setMessage("Introduzca los datos");
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!(String.valueOf(edt.getText()).equals("") || String.valueOf(edt2.getText()).equals(""))) {
                    subirCom(comuni, String.valueOf(edt.getText()), String.valueOf(edt2.getText()));
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

    public void subirCom(String nombreCom, String tit, String desc) {
        final String nombre = nombreCom;
        final String titulo = tit;
        final String descrip = desc;
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(nombreCom);
        dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference drf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(nombre);
                drf.child("Foro").child(correo).push();
                drf.child("Foro").child(correo).child("Titulo").push();
                drf.child("Foro").child(correo).child("Descripcion").push();
                drf.child("Foro").child(correo).child("Titulo").setValue(titulo);
                drf.child("Foro").child(correo).child("Descripcion").setValue(descrip);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



    public void llaves() {
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(comuni).child("Foro");
        dbrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensajes.clear();
                Iterator<DataSnapshot> hijos = dataSnapshot.getChildren().iterator();
                while (hijos.hasNext()) {
                    final Bitmap[] bitmap = new Bitmap[1];
                    DataSnapshot dato = (DataSnapshot) hijos.next();
                    String h = dato.getKey();
                    String titulo = (String) dato.child("Titulo").getValue();
                    String description = (String) dato.child("Descripcion").getValue();
                    titulos.add(titulo);
                    descriptions.add(description);
                    emails.add(h);
                }
                for (int i = 0; i < emails.size(); i++) {
                    try {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://elescalon-79fa4.appspot.com").child("images").child(emails.get(i));
                        final File localFile = File.createTempFile("images", "jpg");
                        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bit = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                fotos.add(bit);
                                if (fotos.size() == titulos.size()) {
                                    for (int i = 0; i < fotos.size(); i++) {
                                        mns = new Mensaje(fotos.get(i), titulos.get(i), descriptions.get(i));
                                        mensajes.add(mns);
                                    }
                                    adaptadorForo = new adaptadorForo(getActivity(), mensajes);
                                    foross.setAdapter(adaptadorForo);
                                }


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
