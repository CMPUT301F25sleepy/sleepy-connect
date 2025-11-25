package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sleepy_connect.CommunityFragment;
import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.eventmanager.EventManagerBottomSheet;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

/**
 * Dialog fragment class for opening entrant lists
 * @author Sam Francisco
 */
public class EntrantManagerBottomSheet extends BottomSheetDialogFragment {

    private Event event;

    public static EntrantManagerBottomSheet newInstance(Event event) {
        EntrantManagerBottomSheet fragment = new EntrantManagerBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        event = (Event) args.getSerializable("event");

        // Inflate the bottom sheet layout
        View view = inflater.inflate(R.layout.bottom_sheet_entrant_manager, container, false);

        // Find buttons from the layout
        TextView tvWaitlist = view.findViewById(R.id.bs_entrant_manager_tv_waitlist);
        TextView tvInvited = view.findViewById(R.id.bs_entrant_manager_tv_invited);
        TextView tvCancelled = view.findViewById(R.id.bs_entrant_manager_tv_cancelled);
        TextView tvEnrolled = view.findViewById(R.id.bs_entrant_manager_tv_enrolled);

        // initialize variable for parent fragment's list label
        TextView listLabel = requireParentFragment().requireView().findViewById(R.id.entrant_manager_tv_list_label);

        // Set click listener for waitlist option
        tvWaitlist.setOnClickListener(v ->  startListFragment(listLabel, "Waitlist"));

        // Set click listener for invited list option
        tvInvited.setOnClickListener(v ->  startListFragment(listLabel, "Invited"));

        // Set click listener for cancelled list option
        tvCancelled.setOnClickListener(v ->  startListFragment(listLabel, "Cancelled"));

        // Set click listener for enrolled list option
        tvEnrolled.setOnClickListener(v ->  startListFragment(listLabel, "Enrolled"));

        return view;
    }

    void startListFragment(TextView listLabel, String newLabel) {

        // set list label
        listLabel.setText(newLabel);

        // determine which fragment is called
        Fragment fragment = null;
        if (newLabel == "Waitlist"){
            fragment = WaitlistFragment.newInstance(event);
        } else if (newLabel == "Invited") {
            fragment = InvitedListFragment.newInstance(event);
        } else if (newLabel == "Cancelled") {
            fragment = CancelledListFragment.newInstance(event);
        } else if (newLabel == "Enrolled") {
            fragment = EnrolledListFragment.newInstance(event);
        }

        assert fragment != null;

        // add enrolled list fragment
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.entrant_manager_fragment_container, fragment, null)
                .commit();

        // Close the bottom sheet
        dismiss();
    }
}