package com.example.sleepy_connect.admin.notificationmanagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sleepy_connect.R;

/**
 * Displays all notifications in the app
 */
public class AdminNotificationListFragment extends Fragment {

    public AdminNotificationListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminNotificationListFragment.
     */
    public static AdminNotificationListFragment newInstance() {
        return new AdminNotificationListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_list, container, false);
    }
}