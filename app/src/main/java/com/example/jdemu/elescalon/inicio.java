package com.example.jdemu.elescalon;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public void entrarForo() {
        Intent intent = new Intent(this, iniComunidad.class);
        startActivity(intent);
    }


}
