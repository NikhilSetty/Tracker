package com.ve.tracker.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ve.tracker.tracker.DBClasses.DbHandler.CustomSettingsDbHandler;
import com.ve.tracker.tracker.Helper.StaticHelper;
import com.ve.tracker.tracker.Models.LocationPointModel;
import com.ve.tracker.tracker.Service.TrackerService;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver receiver;

    Button serviceControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceControl = (Button) findViewById(R.id.buttonStartService);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(StaticHelper.UPDATE_UI_MESSAGE);
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();

            }
        };

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        if(CustomSettingsDbHandler.getIsServiceRunning(getApplicationContext())){
            serviceControl.setText("STOP");
        }else{
            serviceControl.setText("BEGIN");
        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(StaticHelper.UPDATE_UI)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void StartService(View view) {
        Intent intent = new Intent(this, TrackerService.class);
        if(!CustomSettingsDbHandler.getIsServiceRunning(getApplicationContext())) {
            Log.d("TRACKERSERVICE", "Service is not runnning.");
            startService(intent);
            serviceControl.setText("Stop");
            StaticHelper.pointsRecorded = new ArrayList<>();
        }else{
            Log.d("TRACKERSERVICE", "Service is runnning.");
            stopService(intent);
            serviceControl.setText("Begin");
            CustomSettingsDbHandler.setIsServiceRunningFlag(getApplicationContext(), false);
        }
    }

    public void plotMapForObtainedPoints(View view) {
        if(StaticHelper.pointsRecorded.size() > 0){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }
}
