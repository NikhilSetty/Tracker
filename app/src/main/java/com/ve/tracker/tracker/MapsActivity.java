package com.ve.tracker.tracker;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ve.tracker.tracker.Helper.StaticHelper;
import com.ve.tracker.tracker.Utility.LocationUtility;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Polyline line;
    private static final float DEFAULT_ZOOM_LEVEL = 15.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationUtility locationUtility = LocationUtility.getInstance(getApplicationContext());
        if (locationUtility.isGPSEnabled()) {
            Location location = locationUtility.getLastKnownLocation(getApplicationContext());
            if(location!= null){
                Log.i("TRACKERSERVICE", "onStart Location: "+ location.getLatitude()+" : "+ location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude())   , DEFAULT_ZOOM_LEVEL));
            }
        } else {
            locationUtility.showSettingsAlert(MapsActivity.this);
        }

        PlotAllThePointsWithData();
    }

    private void PlotAllThePointsWithData() {
        try{
            if(line != null){
                mMap.clear();
            }

            if(StaticHelper.pointsRecorded.size() > 0){
                for(int i = 0; i < StaticHelper.pointsRecorded.size(); i++) {
                    if (i != StaticHelper.pointsRecorded.size() - 1) {
                        Location srcLoc = StaticHelper.pointsRecorded.get(i).location;
                        LatLng src = new LatLng(srcLoc.getLatitude(), srcLoc.getLongitude());
                        Location destLoc = StaticHelper.pointsRecorded.get(i + 1).location;
                        LatLng dest = new LatLng(destLoc.getLatitude(), destLoc.getLongitude());
                        line = mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(src.latitude, src.longitude),
                                        new LatLng(dest.latitude, dest.longitude))
                                .width(5).color(Color.BLUE).geodesic(true));
                    }
                }
            }
        }catch (Exception e){
            Log.e("TRACKERSERVICE", e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
