package com.jtbosworth.mobilelocationsecurity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewFileListActivity extends AppCompatActivity implements LocationListener, LocationBasedNotification.OnFragmentInteractionListener {
    protected LocationManager locationManager;
    //protected Context context;
    private final int LLD_RANGE = 10;
    private FileQueryManager queryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file_list);
        Log.d("VFLA-OnCreate","Executing OnCreate Function");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.d("VFLA-OnCreate","Location Manager initialized");
        Log.d("VFLA-OnCreate",
                "Location provider enabled: "+locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, LLD_RANGE, this);
            Log.d("VFLA-OnCreate","Requested Location Updates successfully");
        } catch (SecurityException e) {
            Log.e("ViewFileListActivity", "Failed to request location update - Permissions not found");
        }

        /*
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
        */
    }


    //------------------------------------------Location Listener Code ----------------------------


    @Override
    public void onLocationChanged(Location location) {
        Log.d("VFLA-OnLocationChanged","Is context null: "+getApplicationContext().toString());
        queryManager = FileQueryManager.get(getApplicationContext());
        List<MyFile> dbList = queryManager.getFiles();
        List<MyFile> displayList = new ArrayList<MyFile>();
        Log.d("VFLA-OnLocationChanged", "Size of returned list: " + dbList.size());
        for (MyFile file : dbList){
            //Log.d("VFLA-OnLocationChanged","File Location: "+file.getLocation().toString());
            //Log.d("VFLA-OnLocationChanged","Curr Location: "+location.toString());
            //Checks if location is the same
            boolean goodLoc = compareLocations(file.getLocation(),location);
            //If filetype is not locked, return true
            boolean unlocked = file.getFileType().compareTo("Location-Locked") != 0;
            if(goodLoc || unlocked){
                displayList.add(file);
            }
        }
        refreshDisplay(displayList);
    }

    private boolean compareLocations(String fileLocString, Location phoneLoc){
        boolean goodLat = false;
        boolean goodLong = false;
        double MAX_OFFSET = 0.00001;
        double accuracyFactor = 1;
        if(phoneLoc.getAccuracy() > 15 && phoneLoc.getAccuracy() <= 25){
            accuracyFactor = 2;
        }
        if(phoneLoc.getAccuracy() > 25){
            Toast.makeText(getApplicationContext(),"Accuracy is too poor. Hold still and try again", Toast.LENGTH_SHORT);
            //startActivity(new Intent(ViewFileListActivity.this, MainActivity.class));
        }

        String[] fileLocArray = fileLocString.split(" ");
        String[] fileLatLong = fileLocArray[1].split(","); //Lat should be 0, long should be 1
        double fileLat = Double.parseDouble(fileLatLong[0]);
        double fileLong = Double.parseDouble(fileLatLong[1]);
        //Log.d("VFLA-CompareLocations","File Lat: "+fileLat);
        //Log.d("VFLA-CompareLocations","File Long: "+fileLong);

        double phoneLat = phoneLoc.getLatitude();
        double phoneLong = phoneLoc.getLongitude();
        //Log.d("VFLA-CompareLocations","Phone Lat: "+phoneLat);
        //Log.d("VFLA-CompareLocations","Phone Long: "+phoneLong);

        double deltaLat = Math.abs(fileLat - phoneLat);
        double deltaLong = Math.abs(fileLong - phoneLong);
        //Log.d("VFLA-CompareLocations","Delta Lat: "+deltaLat);
        //Log.d("VFLA-CompareLocations","Delta Long:"+deltaLong);

        if(deltaLat < MAX_OFFSET*accuracyFactor){
            goodLat = true;
        }
        if(deltaLong < MAX_OFFSET*accuracyFactor){
            goodLong = true;
        }

        return (goodLat && goodLong);
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
        Log.d("VFLA-RefreshDisplay", "Notification List size: "+notificationList.size());
        Log.d("VFLA-RefreshDisplay", "Locked List size: "+lldList.size());
        Log.d("VFLA-RefreshDisplay", "Regular List size: "+regularList.size());

        //Add UI fragments for each list
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        for(MyFile f : notificationList){
            Fragment fragment = LocationBasedNotification.newInstance(f.getTitle(),f.getId());
            /*
            TextView fragText = (TextView) findViewById(R.id.notifFragmentText);
            if (fragText != null) {
                fragText.setText(f.getTitle());
            }
            */
            fragmentTransaction.add(R.id.locationBasedNotificationsScrollView, fragment);
        }
        for(MyFile f : lldList){
            Fragment fragment = LocationBasedNotification.newInstance(f.getTitle(),f.getId());

            /*
            TextView fragText = (TextView) findViewById(R.id.notifFragmentText);
            if (fragText != null) {
                fragText.setText(f.getTitle());
            }
            */
            fragmentTransaction.add(R.id.locationLockedDocumentScrollView, fragment);
        }
        for(MyFile f : regularList){
            Fragment fragment = LocationBasedNotification.newInstance(f.getTitle(),f.getId());
            /*
            TextView fragText = (TextView) findViewById(R.id.notifFragmentText);
            if (fragText != null) {
                fragText.setText(f.getTitle());
            }
            */
            fragmentTransaction.add(R.id.unlockedDocumentScrollView, fragment);
        }

        fragmentTransaction.commit();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("LocationListener", "Status: " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("LocationListener", "Provider Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("LocationListener","Provider Disabled");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i("FragmentInteraction", "Reached onFragmentInteraction in ViewFileListActivity.java");
    }

    public void onClick(View view){
        Log.i("onClick", "Fragment was clicked");
    }
}
