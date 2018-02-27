package com.example.jdemu.elescalon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;

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
    String name = "";
    SwipeRefreshLayout swipe;

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
            b.remove("numPag");
            b.putInt("numPag", 3);
            swipe = (SwipeRefreshLayout)vista.findViewById(R.id.swiperefresh);
            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipe.setRefreshing(true);
                    swipe.setNestedScrollingEnabled(false);
                    swipe.setEnabled(false);

                    llaves();
                }
            });
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
            foross.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Mensaje mensaje = (Mensaje) foross.getItemAtPosition(i);
                    createMessageDialogo(mensaje);
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
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(nombreCom);

        dbrf.child("Foro").child(correo).push();
        dbrf.child("Foro").child(correo).child("Titulo").push();
        dbrf.child("Foro").child(correo).child("Titulo").setValue(tit);
        dbrf.child("Foro").child(correo).child("Fecha").push();
        dbrf.child("Foro").child(correo).child("Fecha").setValue(""+new Date().getTime());
        dbrf.child("Foro").child(correo).child("Descripcion").push();
        dbrf.child("Foro").child(correo).child("Descripcion").setValue(desc);
        llaves();

    }


    public void llaves() {
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(comuni).child("Foro");
        dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensajes.clear();
                ArrayList<String> emails = new ArrayList();
                final ArrayList<String> titulos = new ArrayList<>();
                final ArrayList<String> descriptions = new ArrayList<>();
                final ArrayList<String> fechas = new ArrayList<>();
                Iterator<DataSnapshot> hijos = dataSnapshot.getChildren().iterator();
                while (hijos.hasNext()) {
                    DataSnapshot dato = (DataSnapshot) hijos.next();
                    String h = dato.getKey();
                    String titulo = (String) dato.child("Titulo").getValue();
                    String description = (String) dato.child("Descripcion").getValue();
                    String fecha=(String)dato.child("Fecha").getValue();
                    titulos.add(titulo + "dataname" + h);
                    descriptions.add(description + "dataname" + h);
                    fechas.add(fecha + "dataname" + h);

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
                                Bitmap bit = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                String titu = "";
                                String desci = "";
                                long fech = 0;
                                String email = storageRef.getName();
                                for (int j = 0; j < titulos.size(); j++) {
                                    String[] ver = titulos.get(j).split("dataname");
                                    if (ver[1].equals(email)) {
                                        titu = ver[0];
                                    }
                                }
                                for (int z = 0; z < descriptions.size(); z++) {
                                    String[] see = descriptions.get(z).split("dataname");
                                    if (see[1].equals(email)) {
                                        desci = see[0];
                                    }
                                }
                                for (int z = 0; z < descriptions.size(); z++) {
                                    String[] see = fechas.get(z).split("dataname");
                                    if (see[1].equals(email)) {
                                        fech = Long.parseLong(see[0]);
                                    }
                                }
                                mns = new Mensaje(bit, titu, desci, email,fech);
                                mensajes.add(mns);
                                Collections.sort(mensajes, new Comparator<Mensaje>() {
                                    @Override
                                    public int compare(Mensaje mensaje, Mensaje t1) {
                                        return new Long(t1.getFecha()).compareTo(new Long(mensaje.getFecha()));
                                    }
                                });
                                adaptadorForo = new adaptadorForo(getActivity(), mensajes);
                                foross.setAdapter(adaptadorForo);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });

                    } catch (IOException e) {

                    }
                    int a =i + 1;
                    //aceptamos barco...por ahora
                    if(a==emails.size()){
                        try {
                            sleep(950);
                            swipe.setRefreshing(false);
                            swipe.setNestedScrollingEnabled(true);
                            swipe.setEnabled(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @SuppressLint("NewApi")
    public void createMessageDialogo(final Mensaje mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.mostramensaje, null);
        builder.setView(v);
        final TextView nombre = (TextView) v.findViewById(R.id.txtNombre);
        ImageView imagen = (ImageView) v.findViewById(R.id.mensajeFoto);
        TextView tituloMensaje = (TextView) v.findViewById(R.id.txtTitulo);
        TextView descripcionMensaje = (TextView) v.findViewById(R.id.txtDescripcion);
        Button responder = (Button) v.findViewById(R.id.btnResponder);
        tituloMensaje.setText(mensaje.getTitulo());
        descripcionMensaje.setText(mensaje.getMSM());
        imagen.setImageBitmap(mensaje.getFoto());
        final AlertDialog ad = builder.show();

        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(comuni).child("Foro");
        dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String[] nom = new String[1];
                nom[0] = "";
                Iterator<DataSnapshot> hijos = dataSnapshot.getChildren().iterator();
                while (hijos.hasNext()) {
                    DataSnapshot dato = (DataSnapshot) hijos.next();
                    String h = dato.getKey();
                    String titulo = (String) dato.child("Titulo").getValue();
                    String description = (String) dato.child("Descripcion").getValue();
                    if (titulo.equals(mensaje.getTitulo()) && description.equals(mensaje.getMSM())) {

                        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(h);
                        dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                nom[0] = dataSnapshot.child("nombre").getValue(String.class);
                                nombre.setText(nom[0]);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (correo.equals(mensaje.getCorreo())) {
            responder.setText("Borrar");
            responder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Borrar comunidad")
                            .setMessage("Desea borrar su mensaje de su foro")
                            .setPositiveButton("ACEPTAR",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DatabaseReference daf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(comuni).child("Foro").child(correo);
                                            daf.removeValue();
                                            llaves();
                                            ad.dismiss();

                                        }
                                    })
                            .setNegativeButton("CANCELAR",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                    builder.create().show();
                }
            });

        } else {
            responder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(vista.getContext());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.responderauser, null);
                    dialogBuilder.setView(dialogView);
                    final EditText respuesta = (EditText) dialogView.findViewById(R.id.editResponderr);
                    dialogBuilder.setTitle("Responder");
                    dialogBuilder.setMessage("Introduzca su respuesta");
                    dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (!(String.valueOf(respuesta.getText()).equals(""))) {
                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                final Map<String, String> mapa = new HashMap<String, String>();
                                mapa.put("usuario", "yo");
                                mapa.put("mensaje", String.valueOf(respuesta.getText()));
                                final Map<String, String> mapa2 = new HashMap<String, String>();
                                DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
                                dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String nombre = dataSnapshot.child("nombre").getValue(String.class);
                                        mapa2.put("usuario", nombre);
                                        mapa2.put("mensaje", String.valueOf(respuesta.getText()));
                                        reference.child("mensajes").child(correo).child(mensaje.getCorreo()).push().setValue(mapa);
                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                                        reference2.child("mensajes").child(mensaje.getCorreo()).child(correo).push().setValue(mapa2);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    });
                    dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()

                    {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //pass
                        }
                    });
                    AlertDialog b = dialogBuilder.create();
                    b.show();


                }
            });
        }


    }

}
