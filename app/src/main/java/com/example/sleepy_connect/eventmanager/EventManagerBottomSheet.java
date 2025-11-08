package com.example.sleepy_connect.eventmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sleepy_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 *
 */
public class EventManagerBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the bottom sheet layout
        View view = inflater.inflate(R.layout.fragment_event_manager_bottom_sheet, container, false);

        // Find buttons from the layout
        TextView tvEditEvent = view.findViewById(R.id.bs_event_manager_tv_edit_event);
        TextView tvManageEntrants = view.findViewById(R.id.bs_event_manager_tv_manage_entrants);

        // Set click listener for edit event
        tvEditEvent.setOnClickListener(v -> {
            // TODO: call edit event
            dismiss(); // Close the bottom sheet
        });

        // Set click listener for manage entrants
        tvManageEntrants.setOnClickListener(v -> {
            // TODO: call manage entrants
            dismiss(); // Close the bottom sheet
        });

        return view;
    }
}