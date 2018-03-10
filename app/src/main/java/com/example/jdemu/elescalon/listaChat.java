package com.example.jdemu.elescalon;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * Created by jdemu on 19/02/2018.
 */

public class listaChat extends Fragment {
    View vista;
    TextView search;
    ListView listaChat;
    ImageView cancel;
    adaptadorListaChat adaptadorChat;
    Bundle b;
    String correo;
    ArrayList<Usuario> usuarios= new ArrayList();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        if (vista != null) {
            ViewGroup parent = (ViewGroup) vista.getParent();
            parent.removeView(vista);
        } else {
            vista = inflater.inflate(R.layout.listachat, container, false);
            search = (TextView) vista.findViewById(R.id.txtBus3);
            cancel = (ImageView) vista.findViewById(R.id.cancel5);
            listaChat=(ListView) vista.findViewById(R.id.lvChat);
            b=getArguments();
            correo=b.getString("correo");
            b.remove("numPag");
            b.putInt("numPag",7);
            llaves();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search.setText("");
                }
            });
            search.clearFocus();

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
                        for (int i = 0; i < usuarios.size(); i++) {
                            Usuario m = usuarios.get(i);
                            int longitud2 = m.getNombre().length();
                            if (longitud2 >= longitud) {
                                String d = m.getNombre().substring(0, longitud).toLowerCase();
                                if (d.equals(c)) {
                                    buscado.add(m);
                                }
                            }
                        }
                        adaptadorChat = new adaptadorListaChat(getActivity(), buscado);
                        listaChat.setAdapter(adaptadorChat);

                    } else {
                        adaptadorChat = new adaptadorListaChat(getActivity(), usuarios);
                        listaChat.setAdapter(adaptadorChat);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            listaChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Usuario user= (Usuario) listaChat.getItemAtPosition(i);
                    ((chatear)getActivity()).Chat(user);
                }
            });
            listaChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final Usuario u= (Usuario) listaChat.getItemAtPosition(i);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle(vista.getResources().getString(R.string.Borrarm))
                            .setMessage(vista.getResources().getString(R.string.Borrarcv)+u.getCorreo()+"?")
                            .setPositiveButton(vista.getResources().getString(R.string.acep),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("mensajes").child(correo).child(u.getCorreo());
                                            dbrf.removeValue();
                                            llaves();

                                        }
                                    })
                            .setNegativeButton(vista.getResources().getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                    builder.create().show();

                    return true;
                }
            });
        }
        return vista;
    }
    public void llaves() {
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("mensajes").child(correo);
        dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarios.clear();
                ArrayList<String> emails = new ArrayList();
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
                                final String email = storageRef.getName();
                                final String[] nombre = new String[1];
                                DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(email);
                                dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        nombre[0] =dataSnapshot.child("nombre").getValue(String.class);
                                        usuarios.add(new Usuario(nombre[0],bit,email));
                                        adaptadorChat = new adaptadorListaChat(getActivity(), usuarios);
                                        listaChat.setAdapter(adaptadorChat);
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
