package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
            envio = (EditText) vista.findViewById(R.id.escritu);
            enviar = (ImageView) vista.findViewById(R.id.envio);
            idNombre = (TextView) vista.findViewById(R.id.idNombre);
            idfoto = (CircleImageView) vista.findViewById(R.id.idFoto);
            scrollView = (ScrollView) vista.findViewById(R.id.deslizantw);
            b = getArguments();
            b.remove("numPag");
            b.putInt("numPag",8);
            micorreo = b.getString("correo");
            correoAmigo = b.getString("amigo");
            nombreAmigo = b.getString("nameAmigo");
            byte[] byteArray = b.getByteArray("Bitmap");
            fotillo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            idNombre.setText(nombreAmigo);
            idfoto.setImageBitmap(fotillo);
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
                        addMessageBox(" You: \n " + message+ " ", 1);
                    } else {
                        addMessageBox(" "+nombreAmigo + ":\n " + message+ " ", 2);


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
            scrollView.fullScroll(View.FOCUS_DOWN);
        }
        return vista;
    }

    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(getActivity());
        textView.setText(message);
        textView.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        } else {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        ly.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
