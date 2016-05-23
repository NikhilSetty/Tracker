package com.ve.tracker.tracker.Utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ve.tracker.tracker.R;

import java.util.List;

import static android.location.LocationManager.GPS_PROVIDER;


/**
 * Created by vikoo on 05/09/15.
 */
public class LocationUtility {
    private Location currentLocation;
    private boolean isGPSEnabled, isNetworkEnabled;
    private LocationManager locationManager;
    private static LocationUtility sLocationUtility;
    private Context mCtx;

    private LocationUtility(Context ctx) {
        mCtx = ctx;
        locationManager = (LocationManager) ctx
                .getSystemService(Context.LOCATION_SERVICE);
    }

    public static LocationUtility getInstance(Context ctx) {
        if (sLocationUtility == null) {
            sLocationUtility = new LocationUtility(ctx);
        }
        return sLocationUtility;
    }

    public void showSettingsAlert(final Activity ctx) {
        /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx, R.style.MaterialDialog);

        Resources res = ctx.getResources();
        // Setting Dialog Title
        alertDialog.setTitle("Enable Location!");

        // Setting Dialog Message
        alertDialog.setMessage("Please enable Location Services to access the feature");

        // On pressing Settings button
        alertDialog.setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctx.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ctx.finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();*/
    }

    public boolean isGPSEnabled() {
        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGPSEnabled || isNetworkEnabled;

    }

    public Location getLastKnownLocation(Context context) {
        try {
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            return loc;
        }catch (SecurityException e){
            return null;
        }
    }

    private boolean checkSelfPermission(String string){
        return true;
    }

    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static Dialog checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                return GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                Log.i("LocationUtility", "This device is not supported.");
                return GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_MISSING, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST);
            }
        }
        return null;
    }

}
