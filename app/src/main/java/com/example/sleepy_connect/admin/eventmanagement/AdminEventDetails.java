package com.example.sleepy_connect.admin.eventmanagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sleepy_connect.R;

/**
 * Displays event details and provides admin event management actions.
 */
public class AdminEventDetails extends Fragment {

    public AdminEventDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminEventDetails.
     */
    public static AdminEventDetails newInstance() {
        return new AdminEventDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_event_details, container, false);
    }
}