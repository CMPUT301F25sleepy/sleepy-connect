package com.example.sleepy_connect;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * class to get the geolocation of a user
 */
public class ObtainGeolocation extends AppCompatActivity {

    // Declare variables
    private FusedLocationProviderClient locationClient;
    private TextView locationText;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_location);

        // Initialize TextView and Button from layout
        locationText = findViewById(R.id.locationText);
        Button getLocationBtn = findViewById(R.id.getLocationBtn);

        // Initialize the location provider client
        locationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set a click listener for the button
        getLocationBtn.setOnClickListener(v -> getCurrentLocation());
    }

    /**
     * Function to get the current location
     */
    private void getCurrentLocation() {
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            /**
             *Function to get the current location
             * @param location location to be displayed
             */
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Get latitude and longitude
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    // Display location in TextView
                    locationText.setText("Latitude: " + lat + "\nLongitude: " + lon);
                } else {
                    // Display error message if location is null
                    locationText.setText("Unable to get location");
                }
            }
        });
    }

    /**
     * Handle the result of the permission request
     * @param requestCode The request code passed in future class to request permissions
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, fetch location
            getCurrentLocation();
        } else {
            // If permission is denied, show message
            locationText.setText("Location permission denied");
        }
    }
}