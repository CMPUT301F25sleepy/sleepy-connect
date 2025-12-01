package com.example.sleepy_connect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationMapDialogFrag extends DialogFragment implements OnMapReadyCallback {

    private double latitude;
    private double longitude;

    public LocationMapDialogFrag(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_map_overlay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.mapContainer, mapFragment)
                .commitNow(); // important: commit immediately so map can attach

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Ensure dialog actually shows its content
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng userLocation = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
        googleMap.addCircle(new CircleOptions().center(userLocation));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
    }
}


