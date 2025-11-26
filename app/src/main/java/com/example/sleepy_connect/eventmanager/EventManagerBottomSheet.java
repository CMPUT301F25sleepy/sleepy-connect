package com.example.sleepy_connect.eventmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.entrantmanagement.EntrantManagerFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Dialog fragment class for showing event management options
 * @author Sam Francisco
 */
public class EventManagerBottomSheet extends BottomSheetDialogFragment {

    public static EventManagerBottomSheet newInstance(Event event) {
        EventManagerBottomSheet fragment = new EventManagerBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the bottom sheet layout
        View view = inflater.inflate(R.layout.fragment_event_manager_bottom_sheet, container, false);

        // Find buttons from the layout
        TextView tvEventDetails = view.findViewById(R.id.bs_event_details);
        TextView tvEditPoster = view.findViewById(R.id.bs_edit_poster);
        TextView tvManageEntrants = view.findViewById(R.id.bs_manage_entrants);

        tvEventDetails.setOnClickListener(v -> {
            Bundle args = getArguments();

            Event event = (Event) args.getSerializable("event");

            OrganizerEventDetailsFragment fragment = OrganizerEventDetailsFragment.newInstance(event);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();

            dismiss();
        });

        // Set click listener for edit event
        tvEditPoster.setOnClickListener(v -> {

            // open edit poster screen
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, EditPosterFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();

            dismiss(); // Close the bottom sheet
        });

        // Set click listener for manage entrants
        tvManageEntrants.setOnClickListener(v -> {
            Bundle args = getArguments();

            Event event = (Event) args.getSerializable("event");

            EntrantManagerFragment fragment = EntrantManagerFragment.newInstance(event);
            // open entrant manager screen
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();

            dismiss(); // Close the bottom sheet
        });

        return view;
    }
}