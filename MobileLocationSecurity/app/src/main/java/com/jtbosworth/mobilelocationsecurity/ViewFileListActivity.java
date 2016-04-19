package com.jtbosworth.mobilelocationsecurity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ViewFileListActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    protected Context context;
    private final int LLD_RANGE = 10;
    private FileQueryManager queryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file_list);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, LLD_RANGE, this);
        } catch (SecurityException e) {
            Log.e("ViewFileListActivity", "Failed to request location update - Permissions not found");
        }
    }


    //------------------------------------------Location Listener Code ----------------------------


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
        List<MyFile> notificationList = new ArrayList<MyFile>();
        List<MyFile> lldList = new ArrayList<MyFile>();
        List<MyFile> regularList = new ArrayList<MyFile>();

        for(MyFile f : displayList){
            switch(f.getFileType()){
                case "Notification":
                    notificationList.add(f);
                    break;
                case "Location-Locked":
                    lldList.add(f);
                    break;
                default:
                    regularList.add(f);
            }
        }
        //Add UI fragments for each list
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("LocationListener", "Status: " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("LocationListener","Provider Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("LocationListener","Provider Disabled");
    }
}
