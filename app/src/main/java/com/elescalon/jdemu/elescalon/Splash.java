package com.elescalon.jdemu.elescalon;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    TextView nombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        nombre=(TextView)findViewById(R.id.NAME);
        String carpetaFuente = "fonts/lemonismDEMO.otf";
        Typeface fuente = Typeface.createFromAsset(getAssets(), carpetaFuente);
        nombre.setTypeface(fuente);
        Thread timerTread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerTread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
