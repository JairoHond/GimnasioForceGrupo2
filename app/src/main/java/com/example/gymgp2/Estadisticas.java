package com.example.gymgp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymgp2.Contextos.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Estadisticas extends AppCompatActivity {

    TextView viewkilomes,viewkalomes,viewsemanterior,viewsemactual,viewmesanterior,viewmesactual;
    ImageView btnatras10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        casteo();

        btnatras10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Menu.class);
                startActivity(intent);
            }
        });

        SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
        String idusuario = mSharedPrefs.getString("idusuario","");
        mostrarEstadisticos(idusuario);

    }

    public void casteo(){
        btnatras10 = (ImageView) findViewById(R.id.btnatras10);
        viewkilomes = (TextView) findViewById(R.id.viewkilomes);
        viewkalomes = (TextView) findViewById(R.id.viewkalomes);
        viewsemanterior = (TextView) findViewById(R.id.viewsemanterior);
        viewsemactual = (TextView) findViewById(R.id.viewsemactual);
        viewmesanterior = (TextView) findViewById(R.id.viewmesanterior);
        viewmesactual = (TextView) findViewById(R.id.viewmesactual);
    }

    private void mostrarEstadisticos(String codigo_usuario) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario", codigo_usuario);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.Estadistico,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray usuarioArray = response.getJSONArray("datos");

                    //listaActividades.clear();//limpiar la lista de usuario antes de comenzar a listar
                    for (int i = 0; i < usuarioArray.length(); i++) {
                        JSONObject Row = usuarioArray.getJSONObject(i);
                        viewkilomes.setText(Row.getString("skmmesact"));
                        viewkalomes.setText(Row.getString("sumkcal"));
                        viewsemanterior.setText(Row.getString("skmsemant"));
                        viewsemactual .setText(Row.getString("skmsemact"));
                        viewmesanterior.setText(Row.getString("skmmesant"));
                        viewmesactual.setText(Row.getString("skmmesact"));
                    }

                }catch (JSONException ex){
                    Toast.makeText(getApplicationContext(), "Upps, hay un error al mostrar las estadisticas: "+ex, Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }



}