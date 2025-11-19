package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.R;

import java.util.ArrayList;

/**
 * Displays the list of cancelled entrants for a specific event
 */
public class CancelledListFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> entrantList;
    private EntrantListAdapter adapter;

    public CancelledListFragment() {
        // Required empty public constructor
    }

    public static CancelledListFragment newInstance() {
        return new CancelledListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cancelled_list, container, false);
        ListViewModel vm = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        listView = view.findViewById(R.id.cancelled_entrant_list);

        entrantList = new ArrayList<>();
        adapter = new EntrantListAdapter(entrantList, requireContext());
        listView.setAdapter(adapter);

        loadCancelledEntrants();

        return view;
    }

    /**
     * Loads cancelled entrant IDs from the EventViewModel and updates the adapter.
     */
    private void loadCancelledEntrants() {
        Event event = EventViewModel.getEvent().getValue();
        if (event == null) {
            return;
        }

        ArrayList<String> cancelled = event.getDeclinedList();

        if (cancelled != null) {
            entrantList.clear();
            entrantList.addAll(cancelled);

            // Rebuild adapter
            adapter = new EntrantListAdapter(entrantList, requireContext());
            listView.setAdapter(adapter);
        }
    }
}