package com.example.sleepy_connect;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    public interface LocationCallback {
        void onLocation(double lat, double lon);
        void onError(String message);
    }

    public static void getUserLocation(Activity activity, LocationCallback callback) {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2001);

            callback.onError("Permission not granted yet");
            return;
        }

        FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(activity);

        client.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                callback.onLocation(location.getLatitude(), location.getLongitude());
            } else {
                callback.onError("Unable to obtain location");
            }
        });
    }
}

