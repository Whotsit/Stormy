package com.example.gaming.stormy.UI;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

/**
 * Created by Gaming on 7/29/2015.
 */
public class GPSTracker extends Service implements LocationListener{

    private Context context;

    boolean isGPSEnabled = false;
    boolean canGetLocation = false;
    boolean isNetworkEnabled = false;

    Location location;

    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 *1;

    protected  LocationManager locationManager;

    public GPSTracker(Context context){
        this.context = context;
        getLocation();
    }

    public Location getLocation(){
        try{
            //initialize location manager
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            //Boolean that checks if there is a gps to provide information
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //Boolean that checks if there is wifi
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled){
                //don't do anything
            }
            else{
                this.canGetLocation = true;
                if(isNetworkEnabled){//Gets updates to the location once every 10 units or minute using WiFi
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if(locationManager != null){//if location manager is initialized, then get the last known location
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            latitude = location.getLatitude(); //sets coordinates
                            longitude = location.getLongitude();
                        }
                    }
                }
                if(isGPSEnabled) {//Using GPS
                    if(location == null){//Gets updates to the location once every 10 units or minute
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if(locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null){
                                latitude = location.getLatitude();//sets coordinates
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }catch(Exception e){

        }
        return location;
    }

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude() {
        if(location != null){latitude = location.getLatitude();}
        return latitude;
    }

    public double getLongitude() {
        if(location != null){longitude = location.getLongitude();}
        return longitude;
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("GPS is setting");
        alertDialog.setMessage("GPS is not enabled. Do you want to enable it in the settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
