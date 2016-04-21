package com.jtbosworth.mobilelocationsecurity;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class ViewFileListActivity extends AppCompatActivity implements LocationListener, LocationBasedNotification.OnFragmentInteractionListener {
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

        Fragment fragment = new LocationBasedNotification();
        Fragment fragment1 = new LocationBasedNotification();
        Fragment fragment2 = new LocationBasedNotification();
        Fragment fragment3 = new LocationBasedNotification();
        Fragment fragment4 = new LocationBasedNotification();
        Fragment fragment5 = new LocationBasedNotification();
        Fragment fragment6 = new LocationBasedNotification();
        Fragment fragment7 = new LocationBasedNotification();
        Fragment fragment8 = new LocationBasedNotification();
        Fragment fragment9 = new LocationBasedNotification();
        Fragment fragment10 = new LocationBasedNotification();
        Fragment fragment11 = new LocationBasedNotification();
        Fragment fragment12 = new LocationBasedNotification();
        Fragment fragment13 = new LocationBasedNotification();
        Fragment fragment14 = new LocationBasedNotification();
        Fragment fragment15 = new LocationBasedNotification();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.locationBasedNotificationsScrollView, fragment);
        fragmentTransaction.add(R.id.locationBasedNotificationsScrollView, fragment1);
        fragmentTransaction.add(R.id.locationBasedNotificationsScrollView, fragment2);
        fragmentTransaction.add(R.id.locationBasedNotificationsScrollView, fragment3);
        fragmentTransaction.add(R.id.locationBasedNotificationsScrollView, fragment4);
        fragmentTransaction.add(R.id.unlockedDocumentScrollView, fragment5);
        fragmentTransaction.add(R.id.unlockedDocumentScrollView, fragment6);
        fragmentTransaction.add(R.id.unlockedDocumentScrollView, fragment7);
        fragmentTransaction.add(R.id.unlockedDocumentScrollView, fragment8);
        fragmentTransaction.add(R.id.unlockedDocumentScrollView, fragment9);
        fragmentTransaction.add(R.id.unlockedDocumentScrollView, fragment10);
        fragmentTransaction.add(R.id.locationLockedDocumentScrollView, fragment11);
        fragmentTransaction.add(R.id.locationLockedDocumentScrollView, fragment12);
        fragmentTransaction.add(R.id.locationLockedDocumentScrollView, fragment13);
        fragmentTransaction.add(R.id.locationLockedDocumentScrollView, fragment14);
        fragmentTransaction.add(R.id.locationLockedDocumentScrollView, fragment15);

        fragmentTransaction.commit();

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

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i("FragmentInteraction", "Reached onFragmentInteraction in ViewFileListActivity.java");
    }
}
