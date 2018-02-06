package com.example.jdemu.elescalon;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jdemu on 15/01/2018.
 */

public class login extends Fragment {
    View vista;
    EditText nombre,contra;
    ImageView ver;
    Button log;
    TextView reg;
    Bundle b;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (vista != null) {
            ViewGroup parent = (ViewGroup) vista.getParent();
            parent.removeView(vista);
        } else {
            vista = inflater.inflate(R.layout.login, container, false);
            nombre=(EditText) vista.findViewById(R.id.nombre);
            b = getArguments();
            b.remove("numPag");
            b.putInt("numPag",0);
            contra=(EditText)vista.findViewById(R.id.contra);
            ver=(ImageView)vista.findViewById(R.id.ver);
            log=(Button)vista.findViewById(R.id.log);
            reg=(TextView) vista.findViewById(R.id.reg);
            String carpetaFuente = "fonts/Letters for Learners.ttf";
            final Typeface fuente = Typeface.createFromAsset(getActivity().getAssets(), carpetaFuente);
            nombre.setTypeface(fuente);
            contra.setTypeface(fuente);
            log.setTypeface(fuente);
            reg.setTypeface(fuente);
            ver.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch ( event.getAction() ) {
                        case MotionEvent.ACTION_DOWN:
                            contra.setInputType(InputType.TYPE_CLASS_TEXT);
                            contra.setSelection(contra.getText().length());
                            contra.setTypeface(fuente);
                            break;
                        case MotionEvent.ACTION_UP:
                            contra.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            contra.setSelection(contra.getText().length());
                            contra.setTypeface(fuente);

                            break;
                    }
                    return true;
                }
            });
            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).pRegis();
                }
            });
            log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(String.valueOf(nombre.getText()).equals("")||String.valueOf(contra.getText()).equals(""))){
                        ((MainActivity)getActivity()).login(String.valueOf(nombre.getText()),String.valueOf(contra.getText()));
                    }else{
                        Toast.makeText(vista.getContext(), "Compruebe que los campos no esten vacios", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
            return vista;
    }
}
