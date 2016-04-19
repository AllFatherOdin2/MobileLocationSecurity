package com.jtbosworth.mobilelocationsecurity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by David on 4/19/2016.
 */
public class MyLocationListener implements LocationListener {
    protected LocationManager locationManager;
    protected Context context;
    private final int LLD_RANGE = 10;
    private FileQueryManager queryManager;

    @Override
    public void onLocationChanged(Location location) {
        queryManager = FileQueryManager.get(context);
        List<MyFile> dbList = queryManager.getFiles();
        List<MyFile> displayList = new ArrayList<MyFile>();
        for (MyFile file : dbList){
            if(file.getLocation().compareTo(location.toString()) == 0){
                displayList.add(file);
            }
        }
        refreshDisplay(displayList);
    }

    private void refreshDisplay(List<MyFile> displayList) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("LocationListener","Status: "+ status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("LocationListener","Provider Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("LocationListener","Provider Disabled");
    }

    public Location getCurrentLocation(LocationManager manager){
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, LLD_RANGE, this);
            return manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e){
            Log.e("LocationListener", "User does not have permission to access locations" );
        }
        return null;
    }
}
