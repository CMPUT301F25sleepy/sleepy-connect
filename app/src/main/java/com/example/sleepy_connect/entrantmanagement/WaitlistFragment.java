package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.sleepy_connect.DrawReplacements;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.DrawReplacements;
import com.example.sleepy_connect.ObtainGeolocation;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.eventmanager.EventManagerBottomSheet;

import java.util.ArrayList;

/**
 * Displays a list of waitlisted entrants for a specific event
 */
public class WaitlistFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> entrantList;
    private EntrantListAdapter adapter;

    public WaitlistFragment() {
        // Required empty public constructor
    }

    public static WaitlistFragment newInstance() {
        return new WaitlistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_waitlist, container, false);
        listView = view.findViewById(R.id.waitlist_entrant_list);

        // Initialize empty list, adapter loads Entrants with DAL
        entrantList = new ArrayList<>();
        adapter = new EntrantListAdapter(entrantList, requireContext());
        listView.setAdapter(adapter);

        setupMapButton(view);
        setupInviteButton(view);

        loadWaitlistedEntrants();

        return view;
    }

    /**
     * Loads waitlisted entrant IDs from the EventViewModel and fills adapter list.
     */
    private void loadWaitlistedEntrants() {
        Event event = EventViewModel.getEvent().getValue();
        if (event == null) {
            return;
        }

        ArrayList<String> waitlisted = event.getWaitingList();

        if (waitlisted != null) {
            entrantList.clear();
            entrantList.addAll(waitlisted);

            // Rebuild adapter to trigger new DAL fetches
            adapter = new EntrantListAdapter(entrantList, requireContext());
            listView.setAdapter(adapter);
        }
    }

    private void setupMapButton(View view) {
        Button viewLocationList = view.findViewById(R.id.waitlist_map_button);

        viewLocationList.setOnClickListener(v -> {
            ObtainGeolocation fragment = new ObtainGeolocation();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setupInviteButton(View view) {
        Button inviteEntrants = view.findViewById(R.id.waitlist_invite_button);

        inviteEntrants.setOnClickListener(v -> {
            Event event = EventViewModel.getEvent().getValue();
            if (event == null) return;

            DrawReplacements replace = new DrawReplacements();
            replace.drawReplacementApp(event);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.waitlist_entrant_list);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // TODO - add option to remove entrant to ALL lists


            // open bottom sheet
            EntrantManagerSelectedBottomSheet bottomSheet = new EntrantManagerSelectedBottomSheet();
            bottomSheet.show(requireActivity().getSupportFragmentManager(), "ModalBottomSheet");
        });

    }
}