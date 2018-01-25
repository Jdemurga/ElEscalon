package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.security.Key;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jdemu on 16/01/2018.
 */

public class perfil extends Fragment {
    View vista;
    Spinner edad, comunidad;
    ImageView ver,foto;
    TextView contra, correoE;
    EditText otros, name, calle;
    String correo;
    Bundle b;
    ConstraintLayout cl;
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
            cl=(ConstraintLayout)vista.findViewById(R.id.pantallaPerfil);
            foto=(ImageView)vista.findViewById(R.id.Pfoto);
            b = getArguments();
            correo = b.getString("correo");
            ArrayList<Integer> edades = new ArrayList();
            for (int i = 0; i < 85; i++) {
                int num = i + 15;
                edades.add(num);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(vista.getContext(), android.R.layout.simple_list_item_1, edades);
            edad.setAdapter(arrayAdapter);
            ArrayList<String> comunidades = new ArrayList();
            for (int i = 0; i < 5; i++) {
                int numero = i + 1;
                comunidades.add("Comunidad " + numero);
            }
            ArrayAdapter adapter = new ArrayAdapter(vista.getContext(), android.R.layout.simple_list_item_1, comunidades);
            comunidad.setAdapter(adapter);
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
            edad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int posicion=edad.getSelectedItemPosition();
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(posicion!=position){
                        actualizarEdad(position);
                        posicion=position;
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
                    if(!hasFocus){
                        closeSoftKeyBoard();
                    }
                }
            });
            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    actualizarNombre(String.valueOf(name.getText()));
                    if(!hasFocus){
                        closeSoftKeyBoard();
                    }
                }
            });
            otros.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    actualizarOtros(String.valueOf(String.valueOf(otros.getText())));
                    if(!hasFocus){
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
                correoE.setText((CharSequence) correo);
                otros.setText(otro[0]);
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
    public void actualizarNombre(String Nuevonombre){
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.child("nombre").setValue(Nuevonombre);
    }
    public void actualizarCalle(String Nuevacalle){
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.child("calle").setValue(Nuevacalle);
    }
    public void actualizarOtros(String NuevoOtros){
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.child("otros").setValue(NuevoOtros);
    }
    public void actualizarEdad(int posicion){
        DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(correo);
        dbrf.child("edad").setValue(posicion+15);
    }
    public void closeSoftKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(vista.getWindowToken(), 0);
    }
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String desencriptar(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(perfil.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue, "utf-8");
        return decryptedValue;

    }
    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(perfil.KEY.getBytes(),perfil.ALGORITHM);
        return key;
    }

}
