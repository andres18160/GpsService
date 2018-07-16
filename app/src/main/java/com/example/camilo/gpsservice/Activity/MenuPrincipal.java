package com.example.camilo.gpsservice.Activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.camilo.gpsservice.Datos.TablaUsuarios;
import com.example.camilo.gpsservice.Entidades.EnUsuario;
import com.example.camilo.gpsservice.R;
import com.example.camilo.gpsservice.Servicios.BackgroundIntentService;
import com.example.camilo.gpsservice.Servicios.BackgroundJobService;

public class MenuPrincipal extends AppCompatActivity {

    private EnUsuario usuario=new EnUsuario();
    //se declara el tool_bar
    private Toolbar toolbar;
    private TablaUsuarios cdUsuariuo=new TablaUsuarios(this);
    private Vibrator vib;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        vib=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        SolicitarPermisosGPSyPhone();
        //se registra el toolbar
        toolbar=(Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Bundle datos=getIntent().getExtras();
        usuario.setNombreDeUsuario(datos.getString("UserName"));



    }

    public void ConfiguracionService(View v){
        vib.vibrate(50);
        Intent i = new Intent(getApplicationContext(),AdminServiceActivity.class);
        i.putExtra("UserName",usuario.getNombreDeUsuario());
        startActivity(i);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }


    @Override public boolean onCreateOptionsMenu(android.view.Menu menu){

        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem opcion_menu){
        int id=opcion_menu.getItemId();
        if(id==R.id.CerrarSession){
            final AlertDialog.Builder builder=new AlertDialog.Builder(MenuPrincipal.this);
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
                    usuario.setEstado("");
                    cdUsuariuo.ActualizarEstadoUsuario(usuario);
                    finish();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
            return true;

        }
        return super.onOptionsItemSelected(opcion_menu);
    }
    public void MensajeToast(String mensaje){
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
    @Override
    public void onBackPressed(){

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




}
