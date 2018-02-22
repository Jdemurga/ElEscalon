package com.example.jdemu.elescalon;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jdemu on 19/02/2018.
 */

public class chat extends Fragment {
    View vista;
    LinearLayout ly;
    EditText envio;
    ImageView enviar;
    TextView idNombre;
    CircleImageView idfoto;
    ScrollView scrollView;
    Bundle b;
    Bitmap fotillo;
    String nombreAmigo, correoAmigo, micorreo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        if (vista != null) {
            ViewGroup parent = (ViewGroup) vista.getParent();
            parent.removeView(vista);
        } else {
            vista = inflater.inflate(R.layout.chat, container, false);
            ly = (LinearLayout) vista.findViewById(R.id.mensajes);
            ly.setFocusable(false);
            envio = (EditText) vista.findViewById(R.id.escritu);
            enviar = (ImageView) vista.findViewById(R.id.envio);
            idNombre = (TextView) vista.findViewById(R.id.idNombre);
            idfoto = (CircleImageView) vista.findViewById(R.id.idFoto);
            scrollView = (ScrollView) vista.findViewById(R.id.deslizantw);
            b = getArguments();
            b.remove("numPag");
            b.putInt("numPag", 8);
            micorreo = b.getString("correo");
            correoAmigo = b.getString("amigo");
            nombreAmigo = b.getString("nameAmigo");
            byte[] byteArray = b.getByteArray("Bitmap");
            fotillo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            idNombre.setText(nombreAmigo);
            idfoto.setImageBitmap(fotillo);
            envio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        enviar.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else {
                        enviar.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                        closeSoftKeyBoard();

                    }
                }
            });
            enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String texto = String.valueOf(envio.getText());
                    if (!texto.equals("")) {
                        final DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference();
                        final Map<String, String> mapa = new HashMap<String, String>();
                        final Map<String, String> mapa2 = new HashMap<String, String>();
                        DatabaseReference dbf = FirebaseDatabase.getInstance().getReference().child("usuarios").child(micorreo);
                        dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String nombre = dataSnapshot.child("nombre").getValue(String.class);
                                mapa.put("usuario", "yo");
                                mapa.put("mensaje", String.valueOf(envio.getText()));
                                mapa2.put("usuario", nombre);
                                mapa2.put("mensaje", String.valueOf(envio.getText()));
                                dbrf.child("mensajes").child(micorreo).child(correoAmigo).push().setValue(mapa);
                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                                reference2.child("mensajes").child(correoAmigo).child(micorreo).push().setValue(mapa2);
                                envio.setText("");

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("mensajes").child(micorreo).child(correoAmigo);
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                    String message = map.get("mensaje").toString();
                    String userName = map.get("usuario").toString();
                    if (userName.equals("yo")) {
                        addMessageBox( message , 1);
                    } else {
                        addMessageBox( message, 2);


                    }

                }


                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        return vista;
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(getActivity());
        textView.setText(message);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setMaxWidth(600);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackground(getResources().getDrawable(R.drawable.redonde3));
            textView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5EB7A7")));
            lp2.setMargins(2, 4, 2, 3);

        } else {
            lp2.gravity = Gravity.LEFT;
            textView.setBackground(getResources().getDrawable(R.drawable.redonde3));
            textView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00796b")));
            lp2.setMargins(2, 4, 2, 3);
        }
        textView.setLayoutParams(lp2);
        ly.addView(textView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void closeSoftKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(vista.getWindowToken(), 0);
    }
}
