package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jdemu on 16/01/2018.
 */

public class registro extends Fragment {
    View vista;
    EditText nombre, contra, repe, gmail;
    Button registar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        if (vista != null) {
            ViewGroup parent = (ViewGroup) vista.getParent();
            parent.removeView(vista);
        } else {
            vista = inflater.inflate(R.layout.registro, container, false);
            nombre = (EditText) vista.findViewById(R.id.rnombre);
            contra = (EditText) vista.findViewById(R.id.rContra);
            repe = (EditText) vista.findViewById(R.id.rRepetir);
            gmail = (EditText) vista.findViewById(R.id.rGmail);
            registar = (Button) vista.findViewById(R.id.regis);
            registar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (String.valueOf(contra.getText()).equals(String.valueOf(repe.getText()))) {
                        if (String.valueOf(contra.getText()).length() >= 6) {
                            ((MainActivity) getActivity()).registrar(String.valueOf(nombre.getText()), String.valueOf(contra.getText()), String.valueOf(gmail.getText()));
                        }else{
                            Toast.makeText(vista.getContext(), "La contraseña debe tener 6 caracteres", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(vista.getContext(), "Compruebe la contraseña", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        return vista;
    }
}
