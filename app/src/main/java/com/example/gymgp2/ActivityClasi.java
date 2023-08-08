package com.example.gymgp2;

import androidx.appcompat.app.AppCompatActivity;
import static java.lang.Double.parseDouble;
import android.content.Context;
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
import com.example.gymgp2.Contextos.Clasificacion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ActivityClasi extends AppCompatActivity {

    AdaptadorClasificacion adaptador;
    ListView listaClasi;
    ImageView btnatras8;
    private final ArrayList<Clasificacion> clasiLista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificacion);

        casteo();

        adaptador = new AdaptadorClasificacion(this);
        //mando a llamar el metodo que me traera el listado de clasificacion
        SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
        String codigo_usuario = mSharedPrefs.getString("idusuario","");
        //String codigo_usuario = mSharedPrefs.getString("codigo_usuario","");

        obtenerlistadoClasificacion(codigo_usuario);

        btnatras8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Menu.class);
                startActivity(intent);
            }
        });
    }

    public void casteo(){
        listaClasi = findViewById(R.id.listaClasi);
        btnatras8 = (ImageView) findViewById(R.id.btnatras8);
    }

    class AdaptadorClasificacion extends ArrayAdapter<Clasificacion> {

        AppCompatActivity appCompatActivity;

        AdaptadorClasificacion(AppCompatActivity context) {
            super(context, R.layout.adapterclasificacion, clasiLista);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.adapterclasificacion, null);
            TextView top, nombrecompleto, kilometro, kcal;
            ImageView foto;

            top = item.findViewById(R.id.clasinum);
            foto = item.findViewById(R.id.clasifoto);
            nombrecompleto = item.findViewById(R.id.clasinombre);
            kilometro = item.findViewById(R.id.clasikilometros);
            kcal = item.findViewById(R.id.clasikcal);

            //Conversion
            double km = 0.0;
            double kc = 0.0;
            km = parseDouble(clasiLista.get(position).getKm());
            kc = parseDouble(clasiLista.get(position).getKcal());

            DecimalFormat df = new DecimalFormat("###.##");

            int enterosKm = Integer.parseInt(Double.toString(km).substring(0, Double.toString(km).indexOf('.')));
            int enterosKc = Integer.parseInt(Double.toString(kc).substring(0, Double.toString(kc).indexOf('.')));
            if(Integer.toString(enterosKm).length()<=3){ kilometro.setText(km+"  "); }
            if(Integer.toString(enterosKc).length()<=3){ kcal.setText(kc+"  "); }
            if(Integer.toString(enterosKm).length()>3){ kilometro.setText(df.format(km/1000)+"K"); }
            if(Integer.toString(enterosKc).length()>3){ kcal.setText(df.format(kc/1000)+"K"); }
            if(Integer.toString(enterosKm).length()>6){ kilometro.setText(df.format(km/1000000)+"M"); }
            if(Integer.toString(enterosKc).length()>6){ kcal.setText(df.format(kc/1000000)+"M"); }
            //Fin Conversion
            top.setText(clasiLista.get(position).getTop());
            nombrecompleto.setText(clasiLista.get(position).getNombrecompleto());
            mostrarFoto(clasiLista.get(position).getFoto(),foto);

            return (item);
        }
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

    private void obtenerlistadoClasificacion(String codigo_usuario) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario", codigo_usuario);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.listaClasifiacion,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray usuarioArray = response.getJSONArray("clasificacion");
                    for (int i = 0; i < usuarioArray.length(); i++) {
                        JSONObject Row = usuarioArray.getJSONObject(i);
                        Clasificacion clasificacion = new Clasificacion(
                                Row.getString("top"),
                                Row.getString("foto"),
                                Row.getString("nombrecompleto"),
                                Row.getString("KmRecorrido"),
                                Row.getString("sumkcal")
                        );
                        clasiLista.add(clasificacion);

                    }
                    listaClasi.setAdapter(adaptador);

                }catch (JSONException ex){
                    Toast.makeText(getApplicationContext(), "Error al mostrar listado de clasificación "+ex, Toast.LENGTH_SHORT).show();
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