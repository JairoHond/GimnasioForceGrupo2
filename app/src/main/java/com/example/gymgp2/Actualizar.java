package com.example.gymgp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymgp2.Contextos.Api;
import com.example.gymgp2.Contextos.Cuentas;
import com.example.gymgp2.Contextos.Pais;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Actualizar extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PICK_IMAGE = 200;
    static final int peticion_captura_imagen =101;
    static final int peticion_acceso_cam=100;
    Uri imageUri;
    ImageView foto4, btnactualizar,btnatras3;
    Button btntomarfoto2, btngaleria2;
    Bitmap imagen2;
    TextView txtfecha2, txtpeso2;
    Spinner sppais2;
    EditText txtnombre2, txtapellido2, txtcorreo4,txttelefono2,txtcontra2;
    ArrayList<String> arrayPaises;
    Pais pais;
    int codigopais, codigo;
    final Context context = this;
    private DatePickerDialog.OnDateSetListener fechalist;
    Intent intent;


    //Variables de perfil para traer datos
    Cuentas usuario;
    ArrayList<String> arrayUsuario;
    String fotoString;
    Bitmap imagen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);


        casteo();

        //
        SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
        String correo = mSharedPrefs.getString("correo","");
        obtenerCuentas(correo);

        txtfecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int anio = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Actualizar.this,
                        android.R.style.Theme_Holo_Light,
                        fechalist,
                        anio,mes,dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        fechalist = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                mes += 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + dia  + "/" + mes + "/" + anio);

                String fecha =  dia  + "/" + mes + "/" + anio;
                txtfecha2.setText(fecha);
            }
        };

        txtpeso2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NumberPicker pes = new NumberPicker(
                        Actualizar.this);
                pes.setMinValue(1);
                pes.setMaxValue(300);
                pes.setValue(160);
                pes.setBackgroundColor(Color.LTGRAY);
                txtpeso2.setText(""+pes.getValue());
                NumberPicker.OnValueChangeListener changedvalue = new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        txtpeso2.setText(""+i1);
                    }
                };

                pes.setOnValueChangedListener(changedvalue);
                AlertDialog.Builder builder = new AlertDialog.Builder(Actualizar.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth).setView(pes);
                builder.setTitle("Peso");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        btnatras3.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(),Menu.class);
               startActivity(intent);
           }
       });

        SpinnerPaises();

        sppais2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cadena = adapterView.getSelectedItem().toString();

                codigopais = Integer.valueOf(extrerCodigo(cadena).toString().replace("]", "").replace("[", ""));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btngaleria2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                abrirGaleria();
            }
        });

        btntomarfoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                permisos();
            }
        });

        //Metodo que setea los campos
        //seteoCampos();

        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
                String correo = mSharedPrefs.getString("correo","");
                ActualizarDatos(correo);
            }
        });



    }



    //ESPACIO IDENTIFICADOR ENTRE METODOS Y ONCREATE





    //Metodo de casteo de elementos
    public void casteo() {

        btntomarfoto2 = (Button) findViewById(R.id.btntomarfoto2);
        btngaleria2 = (Button) findViewById(R.id.btngaleria2);
        btnatras3 = (ImageView) findViewById(R.id.btnatras3);
        foto4 = (ImageView) findViewById(R.id.foto4);
        txtfecha2 = (TextView) findViewById(R.id.txtfecha3);
        txtpeso2 = (TextView) findViewById(R.id.txtpeso3);
        sppais2 = (Spinner) findViewById(R.id.spinnerpais2);
        btnactualizar = (ImageView) findViewById(R.id.btnactualizar2);
        txtnombre2 = (EditText) findViewById(R.id.txtnombre3);
        txtapellido2 = (EditText) findViewById(R.id.txtapellidos3);
        txtcorreo4 = (EditText) findViewById(R.id.txtcorreo4);
        txtcontra2 = (EditText) findViewById(R.id.txtcontra2);
        txttelefono2 = (EditText) findViewById(R.id.txttelefono3);

        sppais2.setEnabled(false);
    }

    /*public void seteoCampos(){
        email = getIntent().getStringExtra("email");
        String nombres =getIntent().getStringExtra("nombres");
        String apellidos =getIntent().getStringExtra("apellidos");
        String fechaNac =getIntent().getStringExtra("fechanac");
        String telefono = getIntent().getStringExtra("telefono");
        String peso =getIntent().getStringExtra("peso");
        fotoString =getIntent().getStringExtra("foto");

        txtnombre2.setText(nombres);
        txtapellido2.setText(apellidos);
        txtfecha2.setText(fechaNac);
        txttelefono2.setText(telefono);
        txtpeso2.setText(peso);
        mostrarFoto(fotoString);
    }*/

    public void mostrarFoto(String foto) {
        try {
            String base64String = "data:image/png;base64,"+foto;
            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            foto4.setImageBitmap(decodedByte);//setea la imagen al imageView
        }catch (Exception ex){
            ex.toString();
        }
    }

    //Obtiene la foto en formato base 64
    private String GetStringImage(Bitmap photo) {

        try {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 70, ba);
            byte[] imagebyte = ba.toByteArray();
            String encode = Base64.encodeToString(imagebyte, Base64.DEFAULT);

            return encode;
        }catch (Exception ex)
        {
            ex.toString();
        }
        return "";
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == peticion_acceso_cam)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                tomarFoto();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Se necesitan permisos",Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //obtener la iamgen por el almacenamiento interno
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE)
        {
            imageUri = data.getData();
            foto4.setImageURI(imageUri);
        }
        //obtener la iamgen por la camara
        if(requestCode == peticion_captura_imagen)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            foto4.setImageBitmap(imagen);
        }
    }




    //Scar los permisos para la camara
    private void permisos() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},peticion_acceso_cam);
        }else{
            tomarFoto();
        }
    }

    //Poder tomar la foto
    private void tomarFoto() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic,peticion_captura_imagen);
        }
    }

    //Poder abrir la galeria
    public void abrirGaleria() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }

    //Obtener el numero del pais
    List<Integer> extrerCodigo(String cadena) {
        List<Integer> todo = new ArrayList<Integer>();
        Matcher finder = Pattern.compile("\\d+").matcher(cadena);
        while (finder.find()) {
            todo.add(Integer.parseInt(finder.group()));
        }
        return todo;
    }


    //Spinner paises
    private void SpinnerPaises(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.EndPointListarPaises,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray contactoArray = jsonObject.getJSONArray( "pais");

                            arrayPaises = new ArrayList<>();
//                            arrayPaises.clear();//limpiar la lista de usuario antes de comenzar a listar
                            for (int i=0; i<contactoArray.length(); i++)
                            {
                                JSONObject RowPais = contactoArray.getJSONObject(i);
                                pais = new Pais(  RowPais.getInt("codigo_pais"),
                                        RowPais.getString("nombre")
                                );

                                arrayPaises.add(pais.getNombre() + " ["+pais.getId()+"]");
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Actualizar.this, android.R.layout.simple_spinner_dropdown_item, arrayPaises);
                            sppais2.setAdapter(adapter);

                            // Establecer el segundo país como valor predeterminado
                            if (arrayPaises.size() > 1) {
                                sppais2.setSelection(1); // El segundo país en el índice 1
                            }


                        }catch (JSONException ex){
                            Toast.makeText(getApplicationContext(), "mensaje "+ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), "mensaje "+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


    private void ActualizarDatos(String correo) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> parametros = new HashMap<>();
        //obtiene la foto tomada o seleccionada, luego verifica en un if si la variable
        // fotoString2 no este vacia ya que en caso que este vacia significa que no se actualiza la foto
        String fotoString2 = GetStringImage(imagen);
        if (fotoString2.equals("")||fotoString2.isEmpty()||fotoString2.equals(null)){
            fotoString2 = fotoString;
        }

        //setear los parametros mediante put
        parametros.put("nombres", txtnombre2.getText().toString());
        parametros.put("apellidos", txtapellido2.getText().toString());
        parametros.put("fecha_nac", txtfecha2.getText().toString());
        parametros.put("telefono",txttelefono2.getText().toString());
        parametros.put("peso", txtpeso2.getText().toString());
        parametros.put("email", correo);
        parametros.put("codigo_pais", codigopais+"");
        parametros.put("foto", fotoString2);

        //Toast.makeText(getApplicationContext(), "String Response2 " + correo, Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.EndPointSetUpdateUser,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), response.getString("mensaje").toString(), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

        Intent intent = new Intent(getApplicationContext(),Menu.class);
        startActivity(intent);
    }









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

                        txtnombre2.setText(usuario.getNombres());
                        txtapellido2.setText(usuario.getApellidos());
                        //sppais2.setText(usuario.getPais());
                        txtfecha2.setText(usuario.getNacimiento());
                        txtpeso2.setText(usuario.getPeso());
                        //txtcorreo2.setText(usuario.getCorreo());
                        txttelefono2.setText(usuario.getTelefono());
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
