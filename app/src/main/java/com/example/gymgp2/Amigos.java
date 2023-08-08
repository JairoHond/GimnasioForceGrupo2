package com.example.gymgp2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

public class Amigos extends AppCompatActivity {
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    ImageView imgAmigo;
    ListView listViewCustomAdapter;
    Cuentas cuentas;
    TextView txtnombreCompleto;
    AdaptadorCuentas adaptador;

    private final ArrayList<Cuentas> listaCuentas = new ArrayList<>();
    final Context context = this;

    int amigo;

    ImageView btnatras5,btnanadir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        casteo();

        btnatras5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),Menu.class);
                startActivity(intencion);
            }
        });
        adaptador = new Amigos.AdaptadorCuentas(this);
        SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
        String idusuario = mSharedPrefs.getString("idusuario","");
        listarcuentas(idusuario);
        btnanadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),AnadirAmigos.class);
                startActivity(intencion);
            }
        });

    }

    public void casteo(){
        btnatras5 = (ImageView) findViewById(R.id.btnatras5);
        btnanadir = (ImageView) findViewById(R.id.btnanadir);
        listViewCustomAdapter = findViewById(R.id.listaAmigos);

    }
    private void listarcuentas(String codUser) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario",codUser);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.EndPointListaAmigosAdd,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray cuentasArray = response.getJSONArray("amigo");

                    listaCuentas.clear();//limpiar la lista de usuario antes de comenzar a listar
                    for (int i = 0; i < cuentasArray.length(); i++) {
                        JSONObject RowCuentas = cuentasArray.getJSONObject(i);
                        cuentas = new Cuentas(RowCuentas.getInt("codigo_usuario"),
                                RowCuentas.getString("nombres"),
                                RowCuentas.getString("apellidos"),
                                RowCuentas.getString("foto"));

                        listaCuentas.add(cuentas);
                    }
                    listViewCustomAdapter.setAdapter(adaptador);

                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), "No tienes amigos en tu lista", Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }


    class AdaptadorCuentas extends ArrayAdapter<Cuentas> {

        AppCompatActivity appCompatActivity;

        AdaptadorCuentas(AppCompatActivity context) {
            super(context, R.layout.amigo, listaCuentas);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.amigo, null);

            imgAmigo = item.findViewById(R.id.imgAmigo);
            mostrarFoto(listaCuentas.get(position).getFoto(),imgAmigo);

            txtnombreCompleto = item.findViewById(R.id.txtNombreAmigo);
            String nombrecompleto= listaCuentas.get(position).getNombres()+" "+listaCuentas.get(position).getApellidos();
            txtnombreCompleto.setText(nombrecompleto);

            ImageView cBox = item.findViewById(R.id.checkBox);
            cBox.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Eliminar amigo");
                    alertDialogBuilder
                            .setMessage("¿Desea Eliminar de amigos a "+nombrecompleto+" ?")
                            .setCancelable(false)
                            .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    eliminarAmigo(Integer.valueOf(Api.codigo_usuario),amigo);
                                    startActivity(new Intent(getApplicationContext(), Amigos.class));
                                    finish();
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    amigo = listaCuentas.get(position).getId();
                }
            });


            return(item);
        }

        public void mostrarFoto(String foto, ImageView Foto) {
            try {
                String base64String = "data:image/png;base64,"+foto;
                String base64Image = base64String.split(",")[1];
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Foto.setImageBitmap(decodedByte);//setea la imagen al imageView
            }catch (Exception ex){
                ex.toString();
            }
        }
    }

    private void eliminarAmigo(int codigoUsuario, int codigoAmigo) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario", codigoUsuario+"");
        parametros.put("codigo_amigo", codigoAmigo+"");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.EndPointEliminarAmigosAdd,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("mensaje").toString().equals("Amigo eliminado")){

                        Toast.makeText(getApplicationContext(), "Se Elimino de tu lista de amigos", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
