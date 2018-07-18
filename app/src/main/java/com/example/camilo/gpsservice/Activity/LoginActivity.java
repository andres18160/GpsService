package com.example.camilo.gpsservice.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Vibrator;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.camilo.gpsservice.Clases.Constant;
import com.example.camilo.gpsservice.Datos.TablaUsuarios;
import com.example.camilo.gpsservice.Entidades.EnUsuario;
import com.example.camilo.gpsservice.Entidades.ResponseLogin;
import com.example.camilo.gpsservice.R;
import com.google.gson.Gson;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class LoginActivity extends AppCompatActivity {
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
    String TAG="login";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
            Intent i = new Intent(getApplicationContext(),MenuPrincipal.class);
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
        btnLoginProgressBar.startAnimation();
        postData();




    }

    public void MensajeToast(String mensaje){
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
    @Override
    public void onBackPressed(){
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.indigo)
                .setButtonsColorRes(R.color.darkDeepOrange)
                .setIcon(R.drawable.logo_pequeno)
                .setTitle("Saliendo de la App")
                .setMessage(R.string.msCerrarApp)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();



    }
    private void requestFocus(View view){
        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void postData(){

        final Gson gson = new Gson();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("UserName",txtUserName.getText().toString());
        params.put("Password",txtPassword.getText().toString());
        RequestQueue requestQueue = Volley.newRequestQueue(contexto);
        JsonObjectRequest req=new JsonObjectRequest (Constant.CIO_URL_LOGIN,new JSONObject(params),new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG,response.toString());
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            if(response.getString("Response")=="null"){
                                MensajeToast((String) getResources().getText(R.string.msUsarioInvalido));
                                btnLoginProgressBar.revertAnimation();
                            }
                            final ResponseLogin loginUser = gson.fromJson(response.getJSONObject("Response").toString(), ResponseLogin.class);
                            enUser=new EnUsuario();
                            enUser.setCodeUser(loginUser.getPersonaId().toString());
                            enUser.setToken(loginUser.getToken());
                            enUser.setBase(loginUser.getBase());
                            enUser.setClave(txtPassword.getText().toString());
                            enUser.setNombreDeUsuario(txtUserName.getText().toString());
                            enUser.setNombres(loginUser.getNombre());
                            enUser.setEstado("Activo");
                            btnLoginProgressBar.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
                            cdUsuario.EliminarUsuario(enUser.getNombreDeUsuario());
                            cdUsuario.GuardarUsuario(enUser);
                            Intent i = new Intent(getApplicationContext(),MenuPrincipal.class);
                            i.putExtra("UserName",enUser.getNombreDeUsuario());
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            btnLoginProgressBar.revertAnimation();
                        }
                    }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            VolleyLog.e(TAG, error.getMessage());
                            btnLoginProgressBar.revertAnimation();
                        }
                });

        btnLoginProgressBar.startAnimation();
        requestQueue.add(req);

    }
}