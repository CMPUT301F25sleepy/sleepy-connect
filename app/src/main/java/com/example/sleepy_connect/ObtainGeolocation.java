package com.example.sleepy_connect;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * class to get the geolocation of a user
 */

public class ObtainGeolocation extends Fragment {

    private FusedLocationProviderClient locationClient;

    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    private Event event;
    private EventDAL eventDAL = new EventDAL();


    public ObtainGeolocation() {
        // Required empty public constructor
    }


    // COULD POTENTIALLY DELETE
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_waitlist, container, false);

        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity());


        return view;
    }

    private Double currentLat = null;
    private Double currentLon = null;

    /**
     * Function to get the current location
     */
    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        locationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                currentLat = location.getLatitude();
                currentLon = location.getLongitude();

                if (event != null) {
                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        double lat = bundle.getDouble("latitude");
                        double lon = bundle.getDouble("longitude");

                        // Create a map with ONLY lat/lng values
                        Map<String, Double> locationMap = new HashMap<>();
                        locationMap.put("lat", lat);
                        locationMap.put("lon", lon);

                        // Add to list
                        event.getLocationsList().add(locationMap);
                    }


                    // Update database
                    eventDAL.updateEvent(event);
                }
            } else {
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == LOCATION_PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        } else {
        }
    }
}
