package com.jtbosworth.mobilelocationsecurity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewFileActivity extends AppCompatActivity implements LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        final RadioGroup rGroup = (RadioGroup) findViewById(R.id.rGroup);
        final EditText information = (EditText) findViewById(R.id.documentContent);
        final EditText title = (EditText) findViewById(R.id.documentTitle);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.d("ViewFileActivity",
                "Location Provider enabled: "+locationManager.isProviderEnabled(locationManager.GPS_PROVIDER));

        //OLD TEST CODE - Used to figure out if the radio group was working.
        /*if (rGroup != null) {
            rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if(null!=rb && checkedId > -1){
                        Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }*/

        if (saveButton != null) {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyFile file = new MyFile();

                    Log.d("ViewFileActivity", "Set information");
                    file.setContent(information.getText().toString());
                    String docTitle = title.getText().toString();
                    boolean titleSet = false;
                    if (docTitle != null && docTitle.compareTo("") != 0) {
                        Log.d("ViewFileActivity", "Set Title");
                        file.setTitle(docTitle);
                        titleSet = true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Please set a title", Toast.LENGTH_SHORT).show();
                    }

                    boolean typeSet = false;
                    Log.d("ViewFileActivity", "RadioButton ID: " + rGroup.getCheckedRadioButtonId());
                    switch(rGroup.getCheckedRadioButtonId()){
                        case R.id.notifButton:
                            Log.d("ViewFileActivity", "Set as Notification");
                            file.setFileType("Notification");
                            typeSet = true;
                            break;
                        case R.id.lockedButton:
                            Log.d("ViewFileActivity", "Set as Locked");
                            file.setFileType("Location-Locked");
                            typeSet = true;
                            break;
                        case R.id.regularButton:
                            Log.d("ViewFileActivity", "Set as Regular");
                            file.setFileType("Regular");
                            typeSet = true;
                            break;
                        default:
                            Toast.makeText(getApplicationContext(),
                                    "Please select a notification type",
                                    Toast.LENGTH_SHORT).show();
                    }

                    Location loc = getCurrentLocation(locationManager);
                    boolean locSet = false;
                    if(loc != null) {
                        Log.d("ViewFileActivity", "Setting Location");
                        file.setLocation(loc.toString());
                        locSet = true;
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Could not determine current location. Please wait a few seconds and try again",
                                Toast.LENGTH_LONG).show();
                    }

                    if(typeSet && titleSet && locSet){
                        Log.d("ViewFileActivity", "Starting new activity");
                        //Log.d("ViewFileActivity", "Location: " + loc.toString());
                        FileQueryManager queryManager = FileQueryManager.get(getApplicationContext());
                        queryManager.addFile(file);
                        startActivity(new Intent(ViewFileActivity.this, MainActivity.class));
                    }
                }
            });
        }
    }

    //--------------------------------------- Location Listener Code ------------------------------
    protected LocationManager locationManager;
    protected Context context;
    private final int LLD_RANGE = 10;
    private FileQueryManager queryManager;

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(),
                "Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude(),
                Toast.LENGTH_SHORT);
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
