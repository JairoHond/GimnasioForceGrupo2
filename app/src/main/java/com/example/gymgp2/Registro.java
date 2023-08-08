package com.example.gymgp2;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymgp2.Contextos.Api;
import com.example.gymgp2.Contextos.Pais;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Registro extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PICK_IMAGE = 200;
    static final int peticion_captura_imagen =101;
    static final int peticion_acceso_cam=100;
    Uri imageUri;
    Session session = null;
    ProgressDialog pdialog = null;
    ImageView btnatras;
    ImageView foto, btnsalvar;
    Button btntomarfoto, btngaleria;
    Bitmap imagen;
    TextView txtfecha, txtpeso;
    Spinner sppais;
    EditText txtnombre, txtapellido, txtcorreo,txttelefono,txtcontra;
    ArrayList<String> arrayPaises;
    Pais pais;
    String para, asunto, mensaje, contra;
    int codigopais, codigo;
    final Context context = this;
    private DatePickerDialog.OnDateSetListener fechalist;
    Intent intent;
    private String correo = "kekojd10@gmail.com";
    private String pass = "bbftisfqrmztehyq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Random random = new Random();
        codigo = random.nextInt(8999) + 1000;

        casteo();

        intent = new Intent(getApplicationContext(), Registro.class);
        txtfecha.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Calendar cal = Calendar.getInstance();
        int anio = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                Registro.this,
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
                txtfecha.setText(fecha);
            }
        };

        txtpeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NumberPicker pes = new NumberPicker(
                   Registro.this);
                pes.setMinValue(1);
                pes.setMaxValue(300);
                pes.setValue(160);
                pes.setBackgroundColor(Color.LTGRAY);
                txtpeso.setText(""+pes.getValue());
                NumberPicker.OnValueChangeListener changedvalue = new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        txtpeso.setText(""+i1);
                    }
                };

                pes.setOnValueChangedListener(changedvalue);
                AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth).setView(pes);
                builder.setTitle("Peso");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            builder.show();
            }
        });

        SpinnerPaises();
        sppais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cadena = adapterView.getSelectedItem().toString();

                codigopais = Integer.valueOf(extrerCodigo(cadena).toString().replace("]", "").replace("[", ""));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),Login.class);
                startActivity(intencion);
            }
        });

        btngaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            abrirGaleria();
            }
        });

        btntomarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            permisos();
            }
        });
    btnsalvar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ValidarCampos();
        }
    });


    }

    //Desde aqui se termina el OnCreate

public void ValidarCampos(){
    if (foto.getDrawable() == null){
        Toast.makeText(getApplicationContext(), "Por favor ingrese una imagen" ,Toast.LENGTH_LONG).show();
    }else if(txtnombre.getText().toString().equals("")){
        Toast.makeText(getApplicationContext(), "Por favor ingrese su nombre" ,Toast.LENGTH_LONG).show();
    }else if(txtapellido.getText().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Por favor ingrese sus apellidos", Toast.LENGTH_LONG).show();
    }else if(txtfecha.getText().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Por favor seleccione su fecha de nacimiento", Toast.LENGTH_LONG).show();
    }else if(sppais.getSelectedItem().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Por favor seleccione su país", Toast.LENGTH_LONG).show();
    }else if(txtpeso.getText().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Por favor seleccione su peso", Toast.LENGTH_LONG).show();
    }else if(txttelefono.getText().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Por favor ingrese su teléfono", Toast.LENGTH_LONG).show();
    }else if(txttelefono.length()<8) {
        Toast.makeText(getApplicationContext(), "Por favor ingrese un teléfono válido", Toast.LENGTH_LONG).show();
    }else if(txtcorreo.getText().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Por favor ingrese su correo electrónico", Toast.LENGTH_LONG).show();
    }else if(txtcontra.getText().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Por favor ingrese una contraseña", Toast.LENGTH_LONG).show();
    }else{
ValidarCorreo();
    }
    }
    public void ConfigurarCorreo(){
        //Almacenamos los datos obtenido en sus respectivas variables para el envio del correo
        para = txtcorreo.getText().toString();
        asunto = "Codigo de Verificación - RUNNING FORCE";
        mensaje = "Hola "+txtnombre.getText().toString()+" "+txtapellido.getText().toString()+", \n"+"Su código de verificación es: "+codigo;

        //creamos las propiedades
        Properties properties = new Properties ();

        //configurando propiedades para email
        //si vamos a utilizar otro servidor tnemos que cambiar los valores
        properties.put("mail.smtp.host", "smtp.gmail.com");//host
        properties.put("mail.smtp.starttls.enable", "true");//Habilitar starttlls de smtp de correo
        properties.put("mail.smtp.port", "587");//puerto
        properties.put("mail.smtp.user", correo);//correo de emisor
        properties.put("mail.smtp.auth", "true");//Autorizacion de envio

        //STARTTLS es una extencion a los protocolos de comunicacion de texto plano,
        //que ofrese una forma de mejorar desde una conexion de exto plano a una conexion cifrada,
        //(TLS O SSL) en lugar de utilizar un puerto diferente para la comunicacion cifrada.

        //Creamos la nueva sesion
        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correo, pass);
            }
        });
        session.setDebug(true);//Esto es para depurar una vez funcione bien lo podemos quitar

        pdialog = ProgressDialog.show(this, "","Enviando correo", true);


        Registro.enviar_correo task = new Registro.enviar_correo(); //llamamos a la clase enviar correo
        task.execute();


    }
    class enviar_correo extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            try {
                //Creando objeto MimeMessage
                MimeMessage message = new MimeMessage(session);
                //Configuracion de la direccion del remitente
                message.setFrom(new InternetAddress(correo));
                //Anadimos el receptor
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(para));
                message.setSubject(asunto);
                message.setText(mensaje);

                //lo enviamos
                Transport t = session.getTransport("smtp");
                t.connect(correo,pass);
                t.sendMessage(message, message.getAllRecipients());

                //cierre
                t.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(),"Mensaje enviado", Toast.LENGTH_LONG).show();
        }
    }

    private String ValidarCorreo() {
        contra = txtcontra.getText().toString();
        ConfigurarCorreo();

        final EditText taskEditText = new EditText(context);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Verifique su correo");
        alertDialogBuilder
                .setMessage("Se ha enviado un código de verificación a " + txtcorreo.getText().toString())
                .setView(taskEditText)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String inputText = taskEditText.getText().toString();
                        if (!inputText.isEmpty()) {
                            try {
                                int task = Integer.parseInt(inputText);
                                if (codigo == task) {
                                    RegistrarUsuario();
                                    Toast.makeText(getApplicationContext(), "Código Válido", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Código Inválido", Toast.LENGTH_SHORT).show();
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(getApplicationContext(), "Ingrese un código numérico válido", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Ingrese un código numérico", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return contra;
    }
    private void RegistrarUsuario() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> parametros = new HashMap<>();

        String fotoString = GetStringImage(imagen);

        parametros.put("nombres", txtnombre.getText().toString());
        parametros.put("apellidos", txtapellido.getText().toString());
        parametros.put("telefono", txttelefono.getText().toString());
        parametros.put("email", txtcorreo.getText().toString());
        parametros.put("clave", contra);
        parametros.put("fecha_nac", txtfecha.getText().toString());
        parametros.put("peso", txtpeso.getText().toString());
        parametros.put("codigo_pais", codigopais+"");
        parametros.put("foto",  fotoString);
        parametros.put("estado","1");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.EndPointCreateUsuario,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "" + response.getString("mensaje").toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                    finish();
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
        limpiar();
    }

    private void limpiar(){
        txtnombre.setText("");
        txtapellido.setText("");
        txttelefono.setText("");
        txtcorreo.setText("");
        txtfecha.setText("");
        txtpeso.setText("");

        }

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

        public void casteo() {

        btntomarfoto = (Button) findViewById(R.id.btntomarfoto2);
        btngaleria = (Button) findViewById(R.id.btngaleria2);
        btnatras = (ImageView) findViewById(R.id.btnatras);
        foto = (ImageView) findViewById(R.id.foto4);
        txtfecha = (TextView) findViewById(R.id.txtfecha3);
        txtpeso = (TextView) findViewById(R.id.txtpeso3);
        sppais = (Spinner) findViewById(R.id.spinnerpais2);
        btnsalvar = (ImageView) findViewById(R.id.btnactualizar2);
        txtnombre = (EditText) findViewById(R.id.txtnombre3);
        txtapellido = (EditText) findViewById(R.id.txtapellidos3);
        txtcorreo = (EditText) findViewById(R.id.txtcorreo4);
        txtcontra = (EditText) findViewById(R.id.txtcontra2);
        txttelefono = (EditText) findViewById(R.id.txttelefono3);

            sppais.setEnabled(false);
    }

    public void abrirGaleria() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }
    private void permisos() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},peticion_acceso_cam);
        }else{
            tomarfoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == peticion_acceso_cam)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                tomarfoto();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Se necesitan permisos",Toast.LENGTH_LONG).show();
        }
    }
    private void tomarfoto(){
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic,peticion_captura_imagen);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //obtener la iamgen por el almacenamiento interno
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE)
        {
            imageUri = data.getData();
            foto.setImageURI(imageUri);
        }
        //obtener la iamgen por la camara
        if(requestCode == peticion_captura_imagen)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            foto.setImageBitmap(imagen);
        }
    }

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

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registro.this, android.R.layout.simple_spinner_dropdown_item, arrayPaises);
                            sppais.setAdapter(adapter);

                            // Establecer el segundo país como valor predeterminado
                            if (arrayPaises.size() > 1) {
                                sppais.setSelection(1); // El segundo país en el índice 1
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

    List<Integer> extrerCodigo(String cadena) {
        List<Integer> todo = new ArrayList<Integer>();
        Matcher finder = Pattern.compile("\\d+").matcher(cadena);
        while (finder.find()) {
            todo.add(Integer.parseInt(finder.group()));
        }
        return todo;
    }
}
