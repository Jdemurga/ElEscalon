package com.elescalon.esj.elescalon;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class inicio extends AppCompatActivity {
    FragmentManager fm = getFragmentManager();
    Fragment fragment;
    FragmentTransaction fts;
    String correo;
    Bundle b;
    DatabaseReference fdb = FirebaseDatabase.getInstance().getReference();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    verMisComu();
                    return true;
                case R.id.navigation_dashboard:
                    verTodos();
                    return true;
                case R.id.navigation_notifications:
                    verPerfil();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        correo = getIntent().getStringExtra("correo");
        b = new Bundle();
        b.putString("correo", correo);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        verMisComu();
    }

    public void verPerfil() {
        fragment = new perfil();
        fts = fm.beginTransaction();
        fts.replace(R.id.container, fragment);
        fragment.setArguments(b);
        fts.commit();
    }

    public void verMisComu() {
        fragment = new Miscomunidades();
        fts = fm.beginTransaction();
        fts.replace(R.id.container, fragment);
        fragment.setArguments(b);
        fts.commit();
    }

    public void verTodos() {
        fragment = new añadirComunidad();
        fts = fm.beginTransaction();
        fragment.setArguments(b);
        fts.replace(R.id.container, fragment);
        fts.commit();
    }

    public void entrarForo(String comunidad,String correo) {
        Intent intent = new Intent(this, iniComunidad.class);
        intent.putExtra("comunidad", comunidad);
        intent.putExtra("correo", correo);
        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            Uri uri=data.getData();
            StorageReference almacen= FirebaseStorage.getInstance().getReference();
            StorageReference img=almacen.child("images").child(uri.getLastPathSegment());
            img.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Se ha subido", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "No se ha subido", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


}
