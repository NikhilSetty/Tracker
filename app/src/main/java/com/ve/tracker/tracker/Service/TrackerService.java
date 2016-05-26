package com.ve.tracker.tracker.Service;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.ve.tracker.tracker.Helper.StaticHelper;
import com.ve.tracker.tracker.Models.LocationPointModel;

import java.util.Date;

public class TrackerService extends Service {

    Thread WorkerThread;

    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    Intent intent;
    int counter = 0;

    // Declaring a Location Manager
    protected LocationManager locationManager;
    LocalBroadcastManager broadcaster;

    public TrackerService() {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        broadcaster = LocalBroadcastManager.getInstance(this);

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("TRACKERSERVICE", "Service Started..");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if(CheckLocationPermissions()) {
            try {
                previousBestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(previousBestLocation == null){
                    previousBestLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
            }catch (SecurityException e) {
            }
        }
        WorkerThread = new Thread(){
            @Override
            public void run(){
                try {
                    Work();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        WorkerThread.start();
        StaticHelper.IsServiceRunning = true;
        return START_STICKY;
    }

    private boolean CheckLocationPermissions() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int resForFineLocation = this.checkCallingOrSelfPermission(permission);
        permission = "android.permission.ACCESS_COARSE_LOCATION";
        int resForCoarseLocation = this.checkCallingOrSelfPermission(permission);
        return ((resForCoarseLocation == PackageManager.PERMISSION_GRANTED) && (resForFineLocation == PackageManager.PERMISSION_GRANTED) );

    }

    private void Work() {
        Log.d("TRACKERSERVICE", "Worker Thread Started.. ");
        // TODO : Monitor GPS and prompt.
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        boolean isDistanceGreaterThanTwoMeters = location.distanceTo(currentBestLocation) > 2;

        // Determine location quality using a combination of timeliness and accuracy
        if(isDistanceGreaterThanTwoMeters) {
            if (isMoreAccurate) {
                return true;
            } else if (isNewer && !isLessAccurate) {
                return true;
            } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                return true;
            }
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("TRACKERSERVICE", "DONE");
        try {
            locationManager.removeUpdates(listener);
        }catch (SecurityException e){
        }
        StaticHelper.IsServiceRunning = false;
    }

    public class MyLocationListener implements LocationListener
    {

        public void onLocationChanged(final Location loc)
        {
            Log.i("TRACKERSERVICE", "Location changed");
            if(isBetterLocation(loc, previousBestLocation)) {
                loc.getLatitude();
                loc.getLongitude();
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);

                Log.d("TRACKERSERVICE", loc.getLatitude() + ", " + loc.getLongitude());
                sendResult("Location obtained : " + loc.getLatitude() + ", " + loc.getLongitude(), loc);
            }else{
                sendResult("Location was not obtained.", null);
            }
        }

        public void sendResult(String message, Location loc) {
            Intent intent = new Intent(StaticHelper.UPDATE_UI);
            if(message != null) {
                intent.putExtra(StaticHelper.UPDATE_UI_MESSAGE, message);
                if(loc != null) {
                    intent.putExtra(StaticHelper.LATTITUBE_LOCATION_OBTAINED, loc.getLatitude());
                    intent.putExtra(StaticHelper.LONGITUDE_LOCATION_OBTAINED, loc.getLongitude());

                    LocationPointModel newPoint = new LocationPointModel();
                    newPoint.location = new Location("");
                    newPoint.location.setLatitude(loc.getLatitude());
                    newPoint.location.setLongitude(loc.getLongitude());
                    newPoint.dateTimeStamp = new Date();

                    StaticHelper.pointsRecorded.add(newPoint);
                }
            }
            broadcaster.sendBroadcast(intent);
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
}

