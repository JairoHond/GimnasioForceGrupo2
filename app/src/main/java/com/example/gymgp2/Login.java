package com.example.gymgp2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymgp2.Contextos.Api;
//import com.example.runninghn.Modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    //Declaracion de elementos
    SharedPreferences preferenciascompartidas,preferenciascompartidaspublicas;
    TextView btncuenta,btnrecuperar;
    Button btniniciar;
    CheckBox chguardar;
    EditText txtcorreo,txtcontra;
    final Context estecontexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Llamado del metodo de casteo
        casteo();

        //Llamado al metodo que hace que no se pierdan los datos por medio del checkbox
        preferenciasCompartidas();

        btniniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = txtcorreo.getText().toString();
                String contrasenia = txtcontra.getText().toString();
                loginUsuario(usuario,contrasenia);

                Api.correo = txtcorreo.getText().toString();

            }
        });

        //boton para crear una cuenta
        btncuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),Registro.class);
                startActivity(intencion);
            }
        });

        //boton para recuperar la contraseña
        btnrecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),Recuperar.class);
                startActivity(intencion);
            }
        });


    }

    //Casteo de elementos
    public void casteo(){
        btnrecuperar = (TextView) findViewById(R.id.btnrecuperar);
        btncuenta = (TextView) findViewById(R.id.btncuenta);
        btniniciar = (Button) findViewById(R.id.btniniciar);
        chguardar = (CheckBox) findViewById(R.id.chguardar);
        txtcorreo = (EditText) findViewById(R.id.txtcorreo4);
        txtcontra = (EditText) findViewById(R.id.txtcontra2);
    }


    public void preferenciasCompartidas(){
        //Haciendo que los datos no se pierda
        preferenciascompartidas = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
        String usuario = preferenciascompartidas.getString("usuario","");
        String contrasenia = preferenciascompartidas.getString("password","");
        txtcorreo.setText(usuario);
        txtcontra.setText(contrasenia);


        //CREDENCIALES PUBLICAS PARA LLAMAR EN LAS ACTIVIDADES Y EVITAR QUE LA INFORMACION SE PIERDA//
        preferenciascompartidaspublicas = getSharedPreferences("credencialesPublicas",Context.MODE_PRIVATE);
        String userPublic = preferenciascompartidaspublicas.getString("usuarioPublic","");

        if (txtcorreo.getText().length() == 0 || txtcontra.getText().length() ==0){
            //Toast.makeText(getApplicationContext(), "Bienvenido ", Toast.LENGTH_SHORT).show();
        }else {
            chguardar.setChecked(true);
            loginUsuario(usuario, contrasenia);
        }

    }



    //Metodo LoginUsuario
    private void loginUsuario(String usuario, String contrasenia) {
        //Una RequestQueue necesita dos cosas a fin de realizar su trabajo: una red mediante la cual transportar las solicitudes y una caché para administrar el almacenamiento en caché  y va de la mano con Volley
        RequestQueue queue = Volley.newRequestQueue(this);
        //Implementación basada en tablas hash de la interfaz Map . Esta implementación proporciona todas las operaciones de mapa opcionales y permite valores nulos y la clave nula .
        //Con el Hash Map capturamos los parametros
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("email", usuario);
        parametros.put("clave", contrasenia);
        //Creamos un objeto de solicitud de JSON con parametros de Posteo de la solicitud y el validar en la API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.EndPointValidarLogin,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            //Ejecuta el metodo para ver si tiene respuesta de que si son los parametros
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray mensajeArray = response.getJSONArray("respuesta");
                    String mensaje="";
                    String codigo="";
                    //Jalar la informacion de la base de datos y validar si estan alli con el RestApiMethod y recorriendolo con el JsonArray.
                    for (int i = 0; i < mensajeArray.length(); i++) {
                        JSONObject RowMensaje = mensajeArray.getJSONObject(i);
                        mensaje = RowMensaje.getString("mensaje");
                        codigo = RowMensaje.getString("codigo_usuario");
                        Api.correo = txtcorreo.getText().toString();
                        Api.codigo_usuario = codigo;
                    }

                    //Si la base de datos me dice que si estan los paramatros me hace otra validacion
                    if (mensaje.equals("login exitoso")){
                        //Accede a las credenciales "contraseña y correo"
                        preferenciascompartidas = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferenciascompartidas.edit();
                        //Cuando se le da el check se Guardan las credenciales si esta chequeado
                        if (chguardar.isChecked()){
                            String user = txtcorreo.getText().toString();
                            String pass = txtcontra.getText().toString();
                            editor.putString("usuario",user);
                            editor.putString("password",pass);
                            editor.commit();
                        }else{
                            //Si no crea entonces limpia los campos.
                            editor.putString("usuario","");
                            editor.putString("password","");
                            editor.commit();
                        }

                        //==============USAR CEDENCIALES PUBLICAMENTE==============///
                            /*Interfaz utilizada para modificar valores en un SharedPreferences objeto. Todos los cambios que realiza en un editor
                            se procesan por lotes y no se copian al original SharedPreferenceshasta que llama commit() oapply() Resumen*/


                        preferenciascompartidaspublicas = getSharedPreferences("credencialesPublicas",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorPublico = preferenciascompartidaspublicas.edit();
                        String userPublic = txtcorreo.getText().toString();
                        editorPublico.putString("correo",txtcorreo.getText().toString());
                        editorPublico.putString("idusuario",codigo );
                        //Dandole una confirmacion a la API que se esta editando de manera publica.

                        //Envia de quetodo esta bien para proceder...
                        editorPublico.commit();


                        //Toast.makeText(getApplicationContext(), "Response " + mensaje, Toast.LENGTH_SHORT).show();
                        //Nos lleva a la actividad de menu
                        Intent intent = new Intent(getApplicationContext(),Menu.class);
                        intent.putExtra("codigo_usuario", codigo);
                        startActivity(intent);
                        finish();
                    }else{
                        //Nos va aparecer una alerta que nos diga que las credenciales estan invalidas y nos aparecera el boton ok que nos quitara el mensaje.
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(estecontexto);
                        alertDialogBuilder.setTitle("Datos invalidos");
                        alertDialogBuilder
                                .setMessage("Verifique su correo o contraseña")
                                .setCancelable(false)
                                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            //Nos valida tirandonos un error que probable la API no este funcionando o el servidor.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);

    }

}