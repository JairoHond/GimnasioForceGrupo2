package com.example.gymgp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymgp2.Contextos.Api;
import com.example.gymgp2.Contextos.Cuentas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MiPerfil extends AppCompatActivity {

    ImageView foto3, btnactualizar2,btnatras7;
    Bitmap imagen2;
    EditText txtnombre3, txtapellido3, txtcorreo3,txttelefono3,txtfecha3, txtpeso3,txtpais3;
    //ArrayList<String> arrayPaises;
    //Pais pais;


    //Variables de perfil para traer datos
    Cuentas usuario;
    ArrayList<String> arrayUsuario;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        casteo();

        SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
        String correo = mSharedPrefs.getString("correo","");
        obtenerCuentas(correo);

        btnatras7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Menu.class);
                startActivity(intent);
            }
        });

        btnactualizar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar();
                finish();
            }
        });

    }

    //Metodo de casteo de elementos
    public void casteo() {
        btnatras7 = (ImageView) findViewById(R.id.btnatras7);
        foto3 = (ImageView) findViewById(R.id.foto4);
        txtfecha3 = (EditText) findViewById(R.id.txtfecha3);
        txtpeso3 = (EditText) findViewById(R.id.txtpeso3);
        btnactualizar2 = (ImageView) findViewById(R.id.btnactualizar2);
        txtnombre3 = (EditText) findViewById(R.id.txtnombre3);
        txtapellido3 = (EditText) findViewById(R.id.txtapellidos3);
        txtpais3 = (EditText) findViewById(R.id.txtpais3);
        txtcorreo3 = (EditText) findViewById(R.id.txtcorreo4);
        txttelefono3 = (EditText) findViewById(R.id.txttelefono3);
    }


    public void mostrarFoto(String foto) {
        try {
            String base64String = "data:image/png;base64,"+foto;
            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            foto3.setImageBitmap(decodedByte);//setea la imagen al imageView
        }catch (Exception ex){
            ex.toString();
        }
    }

    private void editar() {
        Intent intent = new Intent(getApplicationContext(),Actualizar.class);
        intent.putExtra("email", email);
        intent.putExtra("nombres", usuario.getNombres()+"");
        intent.putExtra("apellidos", usuario.getApellidos()+"");
        intent.putExtra("fechanac", usuario.getNacimiento()+"");
        intent.putExtra("codigo_pais", usuario.getIdpais()+"");
        intent.putExtra("telefono", usuario.getTelefono()+"");
        intent.putExtra("peso", usuario.getPeso()+"");
        intent.putExtra("foto", usuario.getFoto()+"").toString();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
        finish();
    }


    //Metodo que obtiene los datos de los perfiles segun el correo

    private void obtenerCuentas(String correo) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("email", correo);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.EndPointBuscarCorreo,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray usuarioArray = response.getJSONArray( "usuario");
                    //JSONArray usuarioArray = response.getJSONArray( "cuentas");

                    arrayUsuario = new ArrayList<>();
                    for (int i=0; i<usuarioArray.length(); i++)
                    {
                        JSONObject RowUsuario = usuarioArray.getJSONObject(i);
                        usuario = new Cuentas(
                                RowUsuario.getInt("codigo_usuario"),
                                RowUsuario.getString("nombres"),
                                RowUsuario.getString("apellidos"),
                                RowUsuario.getString("fecha_nac"),
                                RowUsuario.getString("telefono"),
                                RowUsuario.getString("email"),
                                RowUsuario.getString("peso"),
                                RowUsuario.getString("foto"),
                                RowUsuario.getInt("codigo_pais"),
                                RowUsuario.getString("pais")

                        );

                        txtnombre3.setText(usuario.getNombres());
                        txtapellido3.setText(usuario.getApellidos());
                        txtpais3.setText(usuario.getPais());
                        txtfecha3.setText(usuario.getNacimiento());
                        txtpeso3.setText(usuario.getPeso());
                        txtcorreo3.setText(usuario.getCorreo());
                        txttelefono3.setText(usuario.getTelefono());
                        mostrarFoto(usuario.getFoto().toString());

                    }


                }catch (JSONException ex){
                    Toast.makeText(getApplicationContext(), "Tiene  una excepcion: "+ex, Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Tiene el sig. error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }


}