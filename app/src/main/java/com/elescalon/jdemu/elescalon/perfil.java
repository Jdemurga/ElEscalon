package com.elescalon.jdemu.elescalon;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jdemu on 16/01/2018.
 */

public class perfil extends Fragment {
    View vista;
    Spinner edad, comunidad;
    ImageView ver, foto,mensajesCorreo;
    TextView contra, correoE;
    EditText otros, name, calle;
    String correo;
    Bundle b;
    ConstraintLayout cl;
    ArrayList<String> comunidades;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (vista != null) {
            ViewGroup parent = (ViewGroup) vista.getParent();
            parent.removeView(vista);
        } else {
            vista = inflater.inflate(R.layout.perfil, container, false);
            edad = (Spinner) vista.findViewById(R.id.edad);
            comunidad = (Spinner) vista.findViewById(R.id.comunidad);
            ver = (ImageView) vista.findViewById(R.id.vision);
            name = (EditText) vista.findViewById(R.id.Pnombre);
            calle = (EditText) vista.findViewById(R.id.Pcalle);
            correoE = (TextView) vista.findViewById(R.id.Pcorreo);
            otros = (EditText) vista.findViewById(R.id.Potros);
            contra = (TextView) vista.findViewById(R.id.contras);
            cl = (ConstraintLayout) vista.findViewById(R.id.pantallaPerfil);
            foto = (ImageView) vista.findViewById(R.id.Pfoto);
            b = getArguments();
            correo = b.getString("correo");
            mensajesCorreo=(ImageView)vista.findViewById(R.id.correoMensaje);
            mensajesCorreo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(vista.getContext(), chatear.class);
                    String corre = correo.replace(".", ",");
                    intent.putExtra("correo", corre);
                    startActivity(intent);
                    mensajesCorreo.setImageDrawable(getResources().getDrawable(R.drawable.nmensaje));

                }
            });

            ArrayList<Integer> edades = new ArrayList();
            for (int i = 0; i < 85; i++) {
                int num = i + 15;
                edades.add(num);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(vista.getContext(), android.R.layout.simple_list_item_1, edades);
            edad.setAdapter(arrayAdapter);
            comunidades = new ArrayList();
            leerComn();
            ver.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            contra.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                        case MotionEvent.ACTION_UP:
                            contra.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            break;
                    }
                    return true;
                }
            });
            leerDatos();
            cargarFoto();
            edad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int posicion = edad.getSelectedItemPosition();

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (posicion != position) {
                        actualizarEdad(position);
                        posicion = position;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            calle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    actualizarCalle(String.valueOf(String.valueOf(calle.getText())));
                    if (!hasFocus) {
                        closeSoftKeyBoard();
                    }
                }
            });
            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    actualizarNombre(String.valueOf(name.getText()));
                    if (!hasFocus) {
                        closeSoftKeyBoard();
                    }
                }
            });
            otros.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    actualizarOtros(String.valueOf(String.valueOf(otros.getText())));
                    if (!hasFocus) {
                        closeSoftKeyBoard();

                    }
                }
            });
            cl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    otros.clearFocus();
                    calle.clearFocus();
                    name.clearFocus();
                }
            });
            foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createSingleListDialog();
                }
            });
        }
        return vista;
    }

    public void leerDatos() {
        final String[] nombre = new String[1];
        final String[] contraseña = new String[1];
        final String[] calles = new String[1];
        final int[] edades = new int[1];
        final String[] otro = new String[1];
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombre[0] = dataSnapshot.child("nombre").getValue(String.class);
                contraseña[0] = dataSnapshot.child("contraseña").getValue(String.class);
                calles[0] = dataSnapshot.child("calle").getValue(String.class);
                edades[0] = dataSnapshot.child("edad").getValue(Integer.class);
                otro[0] = dataSnapshot.child("otros").getValue(String.class);
                name.setText(nombre[0]);
                calle.setText(calles[0]);
                correoE.setText((CharSequence) correo.replace(",","."));
                otros.setText(otro[0]);
                mensajeActivo();
                try {
                    contra.setText(desencriptar(contraseña[0]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                edad.setSelection(edades[0] - 15);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void actualizarNombre(String Nuevonombre) {
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.child("nombre").setValue(Nuevonombre);
    }

    public void actualizarCalle(String Nuevacalle) {
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.child("calle").setValue(Nuevacalle);
    }

    public void actualizarOtros(String NuevoOtros) {
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.child("otros").setValue(NuevoOtros);
    }

    public void actualizarEdad(int posicion) {
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.child("edad").setValue(posicion + 15);
    }

    public void closeSoftKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(vista.getWindowToken(), 0);
    }

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";
    public static String desencriptar(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(perfil.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue, "utf-8");
        return decryptedValue;
    }
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(perfil.KEY.getBytes(), perfil.ALGORITHM);
        return key;
    }
    public void createSingleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CharSequence[] items = new CharSequence[2];
        items[0] = vista.getResources().getString(R.string.Camara);
        items[1] =  vista.getResources().getString(R.string.galeria);
        builder.setTitle( vista.getResources().getString(R.string.Opcion))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 2);
                        } else {
                            Intent i = new Intent(Intent.ACTION_PICK);
                            i.setType("image/*");
                            startActivityForResult(i, 1);
                        }
                    }
                });

        builder.create().show();
    }
    public void mensajeActivo(){
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("mensajes").child(correo);
        dbrf.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mensajesCorreo.setImageDrawable(vista.getResources().getDrawable(R.drawable.smensaje));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mensajesCorreo.setImageDrawable(vista.getResources().getDrawable(R.drawable.smensaje));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void cargarFoto() {
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://elescalon-79fa4.appspot.com").child("images").child(correo);
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    foto.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            final Uri uri = data.getData();
            Toast.makeText(vista.getContext(), vista.getResources().getString(R.string.espera), Toast.LENGTH_SHORT).show();
            StorageReference almacen = FirebaseStorage.getInstance().getReference();
            StorageReference img = almacen.child("images").child(correo);
            img.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    foto.setImageURI(uri);
                }
            });
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            final Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageData = stream.toByteArray();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images").child(correo);
            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    foto.setImageBitmap(imageBitmap);
                }
            });

        }
    }

    public void leerComn() {
        final String[] nombres = new String[1];

        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);

        dbrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comunidades.clear();
                nombres[0] = dataSnapshot.child("comunidades").getValue(String.class);
                String[] com = nombres[0].split(";");
                for (int i = 0; i < com.length; i++) {
                    comunidades.add(com[i]);
                }
                ArrayAdapter adapter = new ArrayAdapter(vista.getContext(), android.R.layout.simple_list_item_1, comunidades);
                comunidad.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

