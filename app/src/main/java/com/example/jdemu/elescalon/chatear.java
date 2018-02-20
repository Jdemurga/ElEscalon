package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatear extends AppCompatActivity {
    FragmentManager fm = getFragmentManager();
    Fragment fragment;
    FragmentTransaction fts;
    Bundle b;
    String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatear);
        correo=getIntent().getStringExtra("correo");
        b = new Bundle();
        b.putString("correo",correo);
        listChat();

    }
    public void listChat(){
        fragment = new listaChat();
        fts = fm.beginTransaction();
        fragment.setArguments(b);
        fts.replace(R.id.containe, fragment);
        fts.commit();
    }
    public void Chat(Usuario user){
        fragment = new chat();
        fts = fm.beginTransaction();
        String amigo = user.getCorreo();
        String nombre = user.getNombre();
        Bitmap m=user.getFoto();
        b.putString("amigo",amigo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        m.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        b.putByteArray("Bitmap",byteArray);
        b.putString("nameAmigo",nombre);
        fragment.setArguments(b);
        fts.replace(R.id.containe, fragment);
        fts.commit();
    }
    @Override
    public void onBackPressed() {
        int pag = b.getInt("numPag");
        if (pag == 8) {
            listChat();
        } else {
            super.onBackPressed();

        }
    }

}
