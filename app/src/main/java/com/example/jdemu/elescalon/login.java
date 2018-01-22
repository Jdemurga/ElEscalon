package com.example.jdemu.elescalon;

import android.app.Fragment;
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

/**
 * Created by jdemu on 15/01/2018.
 */

public class login extends Fragment {
    View vista;
    EditText nombre,contra;
    ImageView ver;
    Button log,reg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (vista != null) {
            ViewGroup parent = (ViewGroup) vista.getParent();
            parent.removeView(vista);
        } else {
            vista = inflater.inflate(R.layout.login, container, false);
            nombre=(EditText) vista.findViewById(R.id.nombre);
            contra=(EditText)vista.findViewById(R.id.contra);
            ver=(ImageView)vista.findViewById(R.id.ver);
            log=(Button)vista.findViewById(R.id.log);
            reg=(Button)vista.findViewById(R.id.reg);
            ver.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch ( event.getAction() ) {
                        case MotionEvent.ACTION_DOWN:
                            contra.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                        case MotionEvent.ACTION_UP:
                            contra.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            contra.setSelection(contra.getText().length());
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
                    ((MainActivity)getActivity()).login(String.valueOf(nombre.getText()),String.valueOf(contra.getText()));
                }
            });
        }
            return vista;
    }
}
