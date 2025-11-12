package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sleepy_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Dialog fragment class for opening entrant lists
 * @author Sam Francisco
 */
public class EntrantManagerBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the bottom sheet layout
        View view = inflater.inflate(R.layout.bottom_sheet_entrant_manager, container, false);

        // Find buttons from the layout
        TextView tvWaitlist = view.findViewById(R.id.bs_entrant_manager_tv_waitlist);
        TextView tvInvited = view.findViewById(R.id.bs_entrant_manager_tv_invited);
        TextView tvCancelled = view.findViewById(R.id.bs_entrant_manager_tv_cancelled);
        TextView tvEnrolled = view.findViewById(R.id.bs_entrant_manager_tv_enrolled);

        // Set click listener for waitlist option
        tvWaitlist.setOnClickListener(v -> {

            // open waitlist screen
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.entrant_manager_fragment_container, WaitlistFragment.class, null)
                    .commit();

            dismiss(); // Close the bottom sheet
        });

        // Set click listener for invited list option
        tvInvited.setOnClickListener(v -> {

            // TODO: open invited list screen

            dismiss(); // Close the bottom sheet
        });

        // Set click listener for cancelled list option
        tvCancelled.setOnClickListener(v -> {

            // TODO: open cancelled list screen

            dismiss(); // Close the bottom sheet
        });

        // Set click listener for enrolled list option
        tvEnrolled.setOnClickListener(v -> {

            // TODO: open enrolled list screen

            dismiss(); // Close the bottom sheet
        });

        return view;
    }
}