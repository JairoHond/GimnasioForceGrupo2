package com.example.gymgp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Menu extends AppCompatActivity {

    ImageView btnamigos,btnatras2,btnperfil,btncalificacion, btnprogreso, btnnuevacarrera,btnestadisticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        casteo();

        btnatras2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mSharedPrefs = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                editor.putString("usuario","");
                editor.putString("password","");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnamigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),Amigos.class);
                startActivity(intencion);
            }
        });

       btnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),Actualizar.class);
                startActivity(intencion);
            }
        });

        btncalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),ActivityClasi.class);
                startActivity(intencion);
            }
        });

        btnnuevacarrera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),ActivityNuevaCarrera.class);
                startActivity(intencion);
            }
        });

        btnestadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),Estadisticas.class);
                startActivity(intencion);
            }
        });

    btnprogreso.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intencion = new Intent(getApplicationContext(),Progreso.class);
            startActivity(intencion);
        }
    });
    }

    public void casteo(){
        btnamigos = (ImageView) findViewById(R.id.btnamigos);
        btnatras2 = (ImageView) findViewById(R.id.btnatras2);
        btnperfil = (ImageView) findViewById(R.id.btnperfil);
        btncalificacion = (ImageView) findViewById(R.id.btncalificacion);
        btnnuevacarrera = (ImageView) findViewById(R.id.btnnuevacarrera);
        btnestadisticas = (ImageView) findViewById(R.id.btnestadisticas);
        btnprogreso = (ImageView) findViewById(R.id.btnprogreso);
    }
}