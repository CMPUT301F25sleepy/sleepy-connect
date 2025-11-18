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

import com.example.sleepy_connect.CommunityFragment;
import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.DrawReplacements;
import com.example.sleepy_connect.ObtainGeolocation;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.eventmanager.EventManagerBottomSheet;

import java.util.ArrayList;

/**
 * UNIMPLEMENTED
 * Displays a list of waitlisted entrants for a specific event
 */
public class WaitlistFragment extends Fragment {

    private ListView listView;
    private ArrayList<Entrant> entrantList;
    private EntrantListAdapter adapter;

    public WaitlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WaitlistFragment.
     */
    public static WaitlistFragment newInstance() { return new WaitlistFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        // Get the shared ViewModel instance
        ListViewModel vm = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        View view = inflater.inflate(R.layout.fragment_waitlist, container, false);
        listView = view.findViewById(R.id.waitlist_entrant_list);
        entrantList = vm.getWaitingList().getValue();
        adapter = new EntrantListAdapter(entrantList, getContext());
        listView.setAdapter(adapter);

        // For the view locations button
        Button viewLocationList = view.findViewById(R.id.waitlist_map_button);

        viewLocationList.setOnClickListener(v -> {
            ObtainGeolocation fragment = new ObtainGeolocation();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Calls the current event for use in ExportCSV
        Event event = EventViewModel.getEvent().getValue();

        Button inviteEntrants = view.findViewById(R.id.waitlist_invite_button);
        inviteEntrants.setOnClickListener(v -> {
            DrawReplacements replace = new DrawReplacements();
            assert event != null;
            replace.drawReplacementApp(event);
        });

        return view;
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