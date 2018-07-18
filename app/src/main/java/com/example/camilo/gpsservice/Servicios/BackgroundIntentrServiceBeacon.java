package com.example.camilo.gpsservice.Servicios;
import com.example.camilo.gpsservice.Activity.BeaconActivity;
import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;


public class BackgroundIntentrServiceBeacon extends IntentService implements  BeaconConsumer,RangeNotifier {

    protected final String TAG = BackgroundIntentrServiceBeacon.class.getSimpleName();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final long DEFAULT_SCAN_PERIOD_MS = 6000l;
    private static final String ALL_BEACONS_REGION = "AllBeaconsRegion";
    private  static final String IBEACON_PROTOCOL="m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    private ListView list;
    // Para interactuar con los beacons desde una actividad
    private BeaconManager mBeaconManager;
    private Handler handler;
    public static BeaconActivity UPDATE_LISTENER;

    // Representa el criterio de campos con los que buscar beacons
    private Region mRegion;

    public BackgroundIntentrServiceBeacon() {
        super(BackgroundIntentrServiceBeacon.class.getName());
    }

    public static void setUpdateListener(BeaconActivity activity) {
        UPDATE_LISTENER = activity;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"Servicio Iniciado");
        mBeaconManager = BeaconManager.getInstanceForApplication(this);

        // Fijar un protocolo beacon, Eddystone en este caso
        // mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_PROTOCOL));

        ArrayList<Identifier> identifiers = new ArrayList<>();

        mRegion = new Region(ALL_BEACONS_REGION, identifiers);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

              //  UPDATE_LISTENER.ActualizarDatos(latitud,longitud);
            }
        };

        prepareDetection();

    }

    private void prepareDetection() {

        if (!isLocationEnabled()) {

          //  askToTurnOnLocation();

        } else { // Localización activada, comprobemos el bluetooth

            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Log.d(TAG,"Este dispositivo no soporta bluetooth, no es posible buscar beacons");

            } else if (mBluetoothAdapter.isEnabled()) {

                startDetectingBeacons();

            } else {
                Log.d(TAG," Pedir al usuario que active el bluetooth");
                // Pedir al usuario que active el bluetooth
              //  Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
               // startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }

    /**
     * Empezar a detectar los beacons, ocultando o mostrando los botones correspondientes
     */
    private void startDetectingBeacons() {

        // Fijar un periodo de escaneo
        mBeaconManager.setForegroundScanPeriod(DEFAULT_SCAN_PERIOD_MS);

        // Enlazar al servicio de beacons. Obtiene un callback cuando esté listo para ser usado
        mBeaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            // Empezar a buscar los beacons que encajen con el el objeto Región pasado, incluyendo
            // actualizaciones en la distancia estimada
            mBeaconManager.startRangingBeaconsInRegion(mRegion);
            Log.d(TAG,"Empezando a buscar beacons");

        } catch (RemoteException e) {
            Log.d(TAG, "Se ha producido una excepción al empezar a buscar beacons " + e.getMessage());
        }

        mBeaconManager.addRangeNotifier(this);
    }


    private void stopDetectingBeacons() {

        try {
            mBeaconManager.stopMonitoringBeaconsInRegion(mRegion);
           // showToastMessage(getString(R.string.stop_looking_for_beacons));
        } catch (RemoteException e) {
            Log.d(TAG, "Se ha producido una excepción al parar de buscar beacons " + e.getMessage());
        }

        mBeaconManager.removeAllRangeNotifiers();

        // Desenlazar servicio de beacons
        mBeaconManager.unbind(this);

    }



    /**
     * Comprobar si la localización está activada
     *
     * @return true si la localización esta activada, false en caso contrario
     */
    private boolean isLocationEnabled() {

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean networkLocationEnabled = false;

        boolean gpsLocationEnabled = false;

        try {
            networkLocationEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            gpsLocationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception ex) {
            Log.d(TAG, "Excepción al obtener información de localización");
        }

        return networkLocationEnabled || gpsLocationEnabled;
    }



    /**
     * Mostrar mensaje
     *
     * @param message mensaje a enseñar
     */
    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }



    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons.size() == 0) {
            Log.d(TAG,"No se han encontrado beacons");
        }

        for (Beacon beacon : beacons) {
            //showToastMessage("BeaconId:"+ beacon.getId1()+ "Distancia:"+beacon.getDistance()+" metros");
            Log.d(TAG,"BeaconId:"+ beacon.getId1()+ "Distancia:"+beacon.getDistance()+" metros");
        }

        BootReceiver.scheduleIntentBeacon(getApplicationContext());

    }
}
