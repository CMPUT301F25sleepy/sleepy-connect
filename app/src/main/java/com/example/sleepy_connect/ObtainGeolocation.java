package com.example.sleepy_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.*;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

// Made heavy use of the Android API documentation, saves the location of the app user into an array when called
// Also I have no clue if this even works and I spent so much time on this but at least its a base to build off of
// ALso note this doesnt show anything on a map, it just saved locations to an array, the map is to do
public class ObtainGeolocation extends AppCompatActivity {

    // Permission ID for Android
    private static final int PERMISSION_ID = 44;

    FusedLocationProviderClient fusedLocationProviderClient;
    ArrayList<Location> locationArray = new ArrayList<>();
    LocationCallback locationCallback;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Request location once on startup
        if (checkPermission()) {
            getUserLocationOnce();
        } else {
            requestPermission();
        }
    }

    // Checks if user allows location usage
    private boolean checkPermission() {
        // Needs both fine and coarse location for some reason, even though I just want fine
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Gets the permission from the user to use location
    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                PERMISSION_ID
        );
    }

    @SuppressWarnings("MissingPermission")
    private void getUserLocationOnce() {
        // Gets one location request
        locationRequest = LocationRequest.create();
        locationRequest.setNumUpdates(1); // Only one location update
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // Should abort location fetching once its done
                fusedLocationProviderClient.removeLocationUpdates(this);

                // Checks if the location result isnt null and gets the lat lon
                if (locationResult.getLastLocation() != null) {
                    Location loc = locationResult.getLastLocation();
                    locationArray.add(loc);

                    double lat = loc.getLatitude();
                    double lon = loc.getLongitude();

                    // Show as a toast just to prove it works
                    Toast.makeText(ObtainGeolocation.this,
                            "Location saved: " + lat + ", " + lon,
                            Toast.LENGTH_LONG).show();
                }
            }
        };

        // Needed for the location provider client to function
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                getMainLooper()
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Get the location if the user allows
        if (requestCode == PERMISSION_ID &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            getUserLocationOnce();
        }
        // Else make a toast for debugging cause my brain hurts from trying to understand this
        else {
            Toast.makeText(this,
                    "Permission denied. Cannot obtain location!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

