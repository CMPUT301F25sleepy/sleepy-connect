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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;

public class LocationMapDialogFrag extends DialogFragment implements OnMapReadyCallback {

    private List<Map<String, Double>> locationsList;

    public LocationMapDialogFrag(List<Map<String, Double>> locationsList) {
        this.locationsList = locationsList;
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

    /**
     * Initialize the map fragment
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.mapContainer, mapFragment)
                .commit();

        mapFragment.getMapAsync(this);
    }

    /**
     * On map ready, display all locations on the map
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if (locationsList == null || locationsList.isEmpty()) {
            return;
        }

        // Builder for lat & lng coordinates
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // Display all coordinates on the map
        for (Map<String, Double> loc : locationsList) {
            double lat = loc.get("lat");
            double lng = loc.get("lon");

            LatLng point = new LatLng(lat, lng);
            googleMap.addMarker(new MarkerOptions().position(point));
            builder.include(point);
        }

        LatLngBounds bounds = builder.build();
        // Offset from edges of the map in pixels
        int padding = 100;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }
}
