package com.example.sleepy_connect.admin.profilemanagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sleepy_connect.R;

/**
 * Displays profile details and provides admin profile management actions.
 */
public class AdminProfileDetailsFragment extends Fragment {

    public AdminProfileDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminProfileDetailsFragment.
     */
    public static AdminProfileDetailsFragment newInstance() {
        return new AdminProfileDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_profile_details, container, false);
    }
}