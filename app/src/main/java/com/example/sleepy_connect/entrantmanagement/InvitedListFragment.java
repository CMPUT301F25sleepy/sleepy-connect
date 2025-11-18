package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.R;

import java.util.ArrayList;

/**
 * Displays the list of invited entrants for a specific event
 */
public class InvitedListFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> entrantList;
    private EntrantListAdapter adapter;

    public InvitedListFragment() {
        // Required empty public constructor
    }

    public static InvitedListFragment newInstance() {
        return new InvitedListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_invited_list, container, false);

        listView = view.findViewById(R.id.invited_entrant_list);

        entrantList = new ArrayList<>();
        adapter = new EntrantListAdapter(entrantList, requireContext());
        listView.setAdapter(adapter);

        loadInvitedEntrants();

        return view;
    }

    /**
     * Loads invited entrant IDs from the EventViewModel and fills the adapter list.
     */
    private void loadInvitedEntrants() {
        Event event = EventViewModel.getEvent().getValue();
        if (event == null) return;

        ArrayList<String> invited = event.getPendingList();

        if (invited != null) {
            entrantList.clear();
            entrantList.addAll(invited);

            // Rebuild adapter to trigger DAL
            adapter = new EntrantListAdapter(entrantList, requireContext());
            listView.setAdapter(adapter);
        }
    }
}