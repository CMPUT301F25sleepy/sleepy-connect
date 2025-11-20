package com.example.sleepy_connect.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sleepy_connect.R;
import com.example.sleepy_connect.admin.eventmanagement.AdminEventListFragment;
import com.example.sleepy_connect.admin.notificationmanagement.AdminNotificationListFragment;
import com.example.sleepy_connect.admin.postermanagement.AdminPosterListFragment;
import com.example.sleepy_connect.admin.profilemanagement.AdminProfileListFragment;
import com.example.sleepy_connect.entrantmanagement.CancelledListFragment;
import com.example.sleepy_connect.entrantmanagement.EnrolledListFragment;
import com.example.sleepy_connect.entrantmanagement.InvitedListFragment;
import com.example.sleepy_connect.entrantmanagement.WaitlistFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Dialog fragment class for opening admin lists
 */
public class AdminBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the bottom sheet layout
        View view = inflater.inflate(R.layout.bottom_sheet_admin, container, false);

        // Find buttons from the layout
        TextView tvProfiles = view.findViewById(R.id.bs_admin_tv_profiles);
        TextView tvEvents = view.findViewById(R.id.bs_admin_tv_events);
        TextView tvPosters = view.findViewById(R.id.bs_admin_tv_posters);
        TextView tvNotifications = view.findViewById(R.id.bs_admin_tv_notifications);

        // initialize variable for list label
        TextView listLabel = requireActivity().findViewById(R.id.admin_tv_list_label);

        // Set click listener for waitlist option
        tvProfiles.setOnClickListener(v ->  startListFragment(listLabel, R.string.label_dropdown_profiles, AdminProfileListFragment.class));

        // Set click listener for invited list option
        tvEvents.setOnClickListener(v ->  startListFragment(listLabel, R.string.label_dropdown_events, AdminEventListFragment.class));

        // Set click listener for cancelled list option
        tvPosters.setOnClickListener(v ->  startListFragment(listLabel, R.string.label_dropdown_posters, AdminPosterListFragment.class));

        // Set click listener for enrolled list option
        tvNotifications.setOnClickListener(v ->  startListFragment(listLabel, R.string.label_dropdown_notifications, AdminNotificationListFragment.class));

        return view;
    }

    void startListFragment(TextView listLabel, int newLabelId, Class<? extends Fragment> fragmentClass) {

        // set list label
        listLabel.setText(getResources().getString(newLabelId));

        // add enrolled list fragment
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_container, fragmentClass, null)
                .commit();

        // Close the bottom sheet
        dismiss();
    }
}