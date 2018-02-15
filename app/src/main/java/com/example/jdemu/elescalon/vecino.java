package com.example.jdemu.elescalon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import java.text.DecimalFormat;
import java.util.Iterator;

public class vecino extends AppCompatActivity {
    ImageView imagen;
    TextView name, edad, calle, correo, valor, nvalor,otros;
    ImageView valorar;
    String Ucorreo;
    String comunida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vecino);
        imagen = (ImageView) findViewById(R.id.pvfoto);
        name = (TextView) findViewById(R.id.pvNombre);
        edad = (TextView) findViewById(R.id.pvEdad);
        calle = (TextView) findViewById(R.id.pvCalle);
        correo = (TextView) findViewById(R.id.pvcorreo);
        valor = (TextView) findViewById(R.id.pvValor);
        nvalor = (TextView) findViewById(R.id.pvNvalor);
        valorar = (ImageView) findViewById(R.id.btnValor);
        otros = (TextView) findViewById(R.id.pvotros);
        Ucorreo = getIntent().getExtras().getString("Ucorreo");
        comunida = getIntent().getExtras().getString("comunidadA");
        correo.setText(Ucorreo);
        cargarFoto();
        leerDatos();
        valorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createVslorarDialogo();
            }
        });
    }

    public void leerDatos() {
        final String[] nombre = new String[1];
        final String[] contraseña = new String[1];
        final String[] calles = new String[1];
        final int[] edades = new int[1];
        final String[] otro = new String[1];
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(Ucorreo);
        dbrf.addValueEventListener(new ValueEventListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombre[0] = dataSnapshot.child("nombre").getValue(String.class);
                calles[0] = dataSnapshot.child("calle").getValue(String.class);
                edades[0] = dataSnapshot.child("edad").getValue(Integer.class);
                otro[0] = dataSnapshot.child("otros").getValue(String.class);
                name.setText(nombre[0]);
                calle.setText(calles[0]);
                otros.setText(otro[0]);
                edad.setText("" + edades[0]);
                otros.setMovementMethod(ScrollingMovementMethod.getInstance());
                otros.setScrollBarStyle(0x03000000);
                otros.setVerticalScrollBarEnabled(true);
                otros.setTextColor(0xFF000000);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference dbf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(comunida).child("Participantes").child(Ucorreo);
        dbf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float valoracion=(float)dataSnapshot.child("valoracion").getValue(int.class);
                float numerovaloracion=(float)dataSnapshot.child("numValor").getValue(int.class);
                float media;
                if(numerovaloracion!=0){
                    media= valoracion/numerovaloracion;
                }else{
                    media=0;
                }
                DecimalFormat df= new DecimalFormat("#0.00");
                String numeroConFormato= df.format(media);
                valor.setText(numeroConFormato);
                nvalor.setText(""+(int)(numerovaloracion)+" valoraciones");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cargarFoto() {
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://elescalon-79fa4.appspot.com").child("images").child(Ucorreo);
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imagen.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }
    }
    @SuppressLint("NewApi")
    public void createVslorarDialogo() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.mensajevalorar, null);
        builder.setView(v);
        final RatingBar rb = (RatingBar)v.findViewById(R.id.ratingBar);

        builder.setTitle("Valorar")
                .setMessage("El Mensaje para el usuario")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                DatabaseReference dbf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(comunida).child("Participantes").child(Ucorreo);
                                dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        float valorNuevo= rb.getRating();
                                        int valoracion=dataSnapshot.child("valoracion").getValue(int.class);
                                        int numerovaloracion= dataSnapshot.child("numValor").getValue(int.class);
                                        DecimalFormat df= new DecimalFormat("#0.00");
                                        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("comunidades").child(comunida).child("Participantes").child(Ucorreo);
                                        dbrf.child("valoracion").setValue(valoracion+valorNuevo);
                                        dbrf.child("numValor").setValue(numerovaloracion+1);
                                        float media= (float)(valoracion+valorNuevo)/(numerovaloracion+1);
                                        DecimalFormat dcf= new DecimalFormat("#0.00");
                                        String numeroFormato= dcf.format(media);
                                        valor.setText(numeroFormato);
                                        nvalor.setText(""+(int)(numerovaloracion+1)+" valoraciones");
                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
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


}
