package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    FragmentManager fm = getFragmentManager();
    Fragment fragment;
    FragmentTransaction fts;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    DatabaseReference fdb = FirebaseDatabase.getInstance().getReference();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pLogin();
    }

    public void pLogin() {
        fragment = new login();
        fts = fm.beginTransaction();
        fts.replace(R.id.container, fragment);
        fts.commit();
    }

    public void pRegis() {
        fragment = new registro();
        fts = fm.beginTransaction();
        fts.replace(R.id.container, fragment);
        fts.commit();
    }

    public void iniciar(String correo) {
        Intent intent = new Intent(getApplicationContext(), inicio.class);
        String corre=correo.replace(".",",");
        intent.putExtra("correo", corre);
        startActivity(intent);
    }

    public void registrar(String name, String pass, String gmail) {
        final String nom = name;
        final String con = pass;
        final String email = gmail;
        fauth.createUserWithEmailAndPassword(gmail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Se ha registrado con exito", Toast.LENGTH_SHORT).show();
                    String guardado = email.replace('.', ',');
                    Usuario user = new Usuario(nom, con, 22, "","",email);
                    fdb.child("usuarios").child(guardado).push();
                    fdb.child("usuarios").child(guardado).child("nombre").push();
                    fdb.child("usuarios").child(guardado).child("contraseña").push();
                    fdb.child("usuarios").child(guardado).child("edad").push();
                    fdb.child("usuarios").child(guardado).child("calle").push();
                    fdb.child("usuarios").child(guardado).child("otros").push();
                    fdb.child("usuarios").child(guardado).child("nombre").setValue(user.getNombre());
                    try {
                        fdb.child("usuarios").child(guardado).child("contraseña").setValue(encriptar(user.getContraseña()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fdb.child("usuarios").child(guardado).child("edad").setValue(user.getEdad());
                    fdb.child("usuarios").child(guardado).child("calle").setValue(user.getCalle());
                    fdb.child("usuarios").child(guardado).child("otros").setValue(user.getOtros());
                    pLogin();
                } else {
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(getApplicationContext(), "No se ha podido registrar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void login(String email, String password) {
        final String hot=email;
        fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Se ha iniciado correctamente", Toast.LENGTH_SHORT).show();
                    iniciar(hot);
                } else {
                    Toast.makeText(getApplicationContext(), "No se ha podido iniciar", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encriptar(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(MainActivity.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }


    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(MainActivity.KEY.getBytes(),MainActivity.ALGORITHM);
        return key;
    }
}


