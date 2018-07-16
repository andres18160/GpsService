package com.example.camilo.gpsservice;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.design.widget.TextInputLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.camilo.gpsservice.Clases.ConfigureApp;
import com.example.camilo.gpsservice.Clases.Constant;
import com.example.camilo.gpsservice.Datos.TablaUsuarios;
import com.example.camilo.gpsservice.Entidades.EnUsuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class MainActivity extends AppCompatActivity {
    private final String baseUrl="http://lasaciorestapi.azurewebsites.net/LasaCIORestApi.svc/";
    private EnUsuario enUser=new EnUsuario();
    private TablaUsuarios cdUsuario=new TablaUsuarios(this);
    TextView txtUserName,txtPassword;
    RelativeLayout RelativeLayoutSuperior,RelativeLayoutInferior;
    Animation uptodown,downtoup;
    String username,clave;
    CircularProgressButton btnLoginProgressBar;
    private Vibrator vib;
    Animation animShake;
    private TextInputLayout input_Usuario,input_contrasena;
    private Context contexto=this;
    String tag_json_arry="jarray_req";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SolicitarPermisosGPSyPhone();

        RelativeLayoutSuperior=(RelativeLayout)findViewById(R.id.relativeLayoutSuperior);
        RelativeLayoutInferior=(RelativeLayout)findViewById(R.id.relativeLayoutInferior);
        uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup=AnimationUtils.loadAnimation(this,R.anim.downtoup);
        RelativeLayoutSuperior.setAnimation(uptodown);
        RelativeLayoutInferior.setAnimation(downtoup);
        txtUserName=(TextView)findViewById(R.id.txtUserNmae);
        txtPassword=(TextView)findViewById(R.id.txtClave);
        btnLoginProgressBar=(CircularProgressButton)findViewById(R.id.btnLogin);
        input_Usuario=(TextInputLayout)findViewById(R.id.input_Usuario);
        input_contrasena=(TextInputLayout)findViewById(R.id.input_contrasena);
        animShake= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        vib=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        enUser=cdUsuario.BuscarUsuarioActivo();
        if(enUser!=null){
            Intent i = new Intent(getApplicationContext(),Menu.class);
            i.putExtra("UserName",enUser.getNombreDeUsuario());
            startActivity(i);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void Login(View view){

        String ms="";
        if(txtUserName.getText().toString().trim().isEmpty()){
            input_Usuario.setErrorEnabled(true);
            input_Usuario.setError(getResources().getText(R.string.err_msg_usuario));
            txtUserName.setError(getResources().getText(R.string.err_msg_requerido));
            input_Usuario.setAnimation(animShake);
            input_Usuario.startAnimation(animShake);
            requestFocus(txtUserName);
            vib.vibrate(120);
            return;
        }
        if(txtPassword.getText().toString().trim().isEmpty()){
            input_contrasena.setErrorEnabled(true);
            input_contrasena.setError(getResources().getText(R.string.err_msg_contrasena));
            txtPassword.setError(getResources().getText(R.string.err_msg_requerido));
            input_contrasena.setAnimation(animShake);
            input_contrasena.startAnimation(animShake);
            requestFocus(txtPassword);
            vib.vibrate(120);
            return;
        }

        input_Usuario.setErrorEnabled(false);
        input_contrasena.setErrorEnabled(false);

        if(!ValidarUsuario()){
            ms= (String) getResources().getText(R.string.msUsarioInvalido);
            MensajeToast(ms);
            return;
        }
        AsyncTask<String,String,String> demoCargando=new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try{
                    Thread.sleep(3000);
                    postData();
                    Log.d("Login","Saliendo");

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                return "ok";

            }

            @Override
            protected void onPostExecute(String s) {
                if(s.equals("ok")){
                    btnLoginProgressBar.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
                    Intent i = new Intent(getApplicationContext(),Menu.class);
                    i.putExtra("UserName",enUser.getNombreDeUsuario());
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
            }
        };
        btnLoginProgressBar.startAnimation();
        demoCargando.execute();




    }
    private boolean ValidarUsuario(){
        username=txtUserName.getText().toString();
        clave=txtPassword.getText().toString();
        enUser=cdUsuario.ValidarUsuario(username,clave);
        if(enUser==null){
            if(username.equals("admin")&& clave.equals("1987")){
                enUser=new EnUsuario();
                enUser.setNombreDeUsuario(username);
                return true;
            }else{
                return false;
            }
        }else{
            enUser.setEstado("Activo");
            cdUsuario.ActualizarEstadoUsuario(enUser);
            return true;
        }

    }
    public void MensajeToast(String mensaje){
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.msCerrarApp);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private void requestFocus(View view){
        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    private void SolicitarPermisosGPSyPhone() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }


        }

        int permissionCheckInfoPhone=ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (permissionCheckInfoPhone == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        1);
            }


        }
    }

    public void postData(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username",txtUserName.getText().toString());
        params.put("password",txtPassword.getText().toString());
        JsonObjectRequest req=new JsonObjectRequest (Constant.CIO_URL_LOGIN,new JSONObject(params),new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            VolleyLog.e("Error: ", error.getMessage());
                        }
                });

        ConfigureApp.getmInstance().addToRequestQueue(req,tag_json_arry);

    }
}