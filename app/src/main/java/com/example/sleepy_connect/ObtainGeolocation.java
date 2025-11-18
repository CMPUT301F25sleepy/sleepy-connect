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

/**
 * class to get the geolocation of a user
 */

public class ObtainGeolocation extends Fragment {

    private FusedLocationProviderClient locationClient;
    private TextView locationText;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    public ObtainGeolocation() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_location, container, false);

        locationText = view.findViewById(R.id.locationText);
        Button getLocationBtn = view.findViewById(R.id.getLocationBtn);

        // NEW BUTTON TO SHOW LOCATION ON MAP
        Button showMapBtn = view.findViewById(R.id.showMapBtn);

        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        getLocationBtn.setOnClickListener(v -> getCurrentLocation());

        // Show the map dialog
        showMapBtn.setOnClickListener(v -> {
            if (currentLat != null && currentLon != null) {
                new LocationMapDialogFrag(currentLat, currentLon)
                        .show(getChildFragmentManager(), "mapDialog");
            } else {
                locationText.setText("Get location first");
            }
        });

        return view;
    }

    private Double currentLat = null;
    private Double currentLon = null;

    /**
     * Function to get the current location
     */
    private void getCurrentLocation() {
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

                locationText.setText("Latitude: " + currentLat + "\nLongitude: " + currentLon);
            } else {
                locationText.setText("Unable to get location");
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
            locationText.setText("Location permission denied");
        }
    }
}
