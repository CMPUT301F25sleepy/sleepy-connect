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
import android.widget.Toast;

import com.example.sleepy_connect.DrawReplacements;
import com.example.sleepy_connect.Entrant;
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
    private Event event;

    public WaitlistFragment() {
        // Required empty public constructor
    }

    public static WaitlistFragment newInstance(Event event) {
        WaitlistFragment fragment = new WaitlistFragment();
        Bundle args = new Bundle();
        args.putSerializable("event",event);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get event from viewmodel
        EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        event = vmEvent.getEvent().getValue();
        assert event != null;

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

        // get event from viewmodel
        EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        event = vmEvent.getEvent().getValue();
        assert event != null;

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

        // Determines whether the event has geolocation enabled, if so allow them to view map
        if (event.isGeolocationEnabled()) {
            viewLocationList.setOnClickListener(v -> {
                ObtainGeolocation fragment = new ObtainGeolocation();

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
        // ...or deny if they have geolocation disabled
        else {
            viewLocationList.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Geolocation Disabled", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setupInviteButton(View view) {
        Button inviteEntrants = view.findViewById(R.id.waitlist_invite_button);

        inviteEntrants.setOnClickListener(v -> {

            // get event from viewmodel
            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
            event = vmEvent.getEvent().getValue();
            assert event != null;

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

            // retrieve entrant from list
            String selectedEntrant = entrantList.get(position);

            // open bottom sheet
            EntrantManagerSelectedBottomSheet bottomSheet = EntrantManagerSelectedBottomSheet.newInstance("Waiting");
            bottomSheet.show(getParentFragmentManager()  , "ModalBottomSheet");
        });
    }


}