package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    FragmentManager fm = getFragmentManager();
    Fragment fragment;
    FragmentTransaction fts;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    DatabaseReference fdb = FirebaseDatabase.getInstance().getReference();
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = new Bundle();
        pLogin();
    }

    public void pLogin() {
        fragment = new login();
        fts = fm.beginTransaction();
        fragment.setArguments(b);
        fts.replace(R.id.container, fragment);
        fts.commit();
    }

    public void pRegis() {
        fragment = new registro();
        fts = fm.beginTransaction();
        fragment.setArguments(b);
        fts.replace(R.id.container, fragment);
        fts.commit();
    }

    public void iniciar(String correo) {
        Intent intent = new Intent(getApplicationContext(), inicio.class);
        String corre = correo.replace(".", ",");
        intent.putExtra("correo", corre);
        startActivity(intent);
    }

    public void registrar(String name, String pass, String gmail) {
        Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.foto);
        final Bitmap b= bit;
        final String nom = name;
        final String con = pass;
        final String email = gmail;
        fauth.createUserWithEmailAndPassword(gmail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String guardado = email.replace('.', ',');
                    fdb.child("usuarios").child(guardado).push();
                    fdb.child("usuarios").child(guardado).child("nombre").push();
                    fdb.child("usuarios").child(guardado).child("contraseña").push();
                    fdb.child("usuarios").child(guardado).child("edad").push();
                    fdb.child("usuarios").child(guardado).child("calle").push();
                    fdb.child("usuarios").child(guardado).child("otros").push();
                    fdb.child("usuarios").child(guardado).child("comunidades").push();
                    fdb.child("usuarios").child(guardado).child("nombre").setValue(nom);
                    try {
                        fdb.child("usuarios").child(guardado).child("contraseña").setValue(encriptar(con));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fdb.child("usuarios").child(guardado).child("edad").setValue(22);
                    fdb.child("usuarios").child(guardado).child("calle").setValue("");
                    fdb.child("usuarios").child(guardado).child("otros").setValue("");
                    fdb.child("usuarios").child(guardado).child("comunidades").setValue("");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] imageData = stream.toByteArray();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    String coma=email.replace(".",",");
                    StorageReference imageRef = storageRef.child("images").child(coma);
                    UploadTask uploadTask = imageRef.putBytes(imageData);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.RegExito), Toast.LENGTH_SHORT).show();
                            pLogin();
                        }

                    });

                } else {
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.RegFallo), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encriptar(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(MainActivity.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }


    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(MainActivity.KEY.getBytes(), MainActivity.ALGORITHM);
        return key;
    }

    @Override
    public void onBackPressed() {
        int pag = b.getInt("numPag");
        if (pag == 1) {
            pLogin();
        } else {
            super.onBackPressed();

        }
    }
}


