package com.elescalon.esj.elescalon;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class iniComunidad extends AppCompatActivity {

    FragmentManager fm = getFragmentManager();
    Fragment fragment;
    FragmentTransaction fts;
    String laComunidad;
    Bundle b;
    BottomNavigationView navigation;
    String correo;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    verForo();
                    return true;
                case R.id.navigation_dashboard:
                    verParticipantes();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ini_comunidad);
        laComunidad = getIntent().getStringExtra("comunidad");
        correo=getIntent().getStringExtra("correo");
        b = new Bundle();
        b.putString("comuni",laComunidad);
        b.putString("correo",correo);
        navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        verForo();
    }
    public void verForo(){
        fragment = new foroMns();
        fts = fm.beginTransaction();
        fts.replace(R.id.container, fragment);
        fragment.setArguments(b);
        fts.commit();
    }
    public void verParticipantes(){
        fragment = new Participantes();
        fts = fm.beginTransaction();
        fts.replace(R.id.container, fragment);
        fragment.setArguments(b);
        fts.commit();
    }
    @Override
    public void onBackPressed() {
        int pag = b.getInt("numPag");
        if (pag == 4) {
            verForo();
            navigation.setSelectedItemId(R.id.navigation_home);
        } else {
            super.onBackPressed();

        }
    }


}
