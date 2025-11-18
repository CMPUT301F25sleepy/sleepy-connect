package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.ExportCSV;
import com.example.sleepy_connect.R;

import java.util.ArrayList;

/**
 * Displays the list of enrolled entrants for a specific event
 */
public class EnrolledListFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> entrantList;
    private EntrantListAdapter adapter;

    public EnrolledListFragment() {
        // Required empty constructor
    }

    public static EnrolledListFragment newInstance() {
        return new EnrolledListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enrolled_list, container, false);

        listView = view.findViewById(R.id.enrolled_entrant_list);

        // adapter loads Entrants with dal
        entrantList = new ArrayList<>();
        adapter = new EntrantListAdapter(entrantList, requireContext());
        listView.setAdapter(adapter);

        loadEnrolledEntrants();

        setupCSVButton(view);

        return view;
    }

    /**
     * Loads enrolled entrant IDs from the EventViewModel and fills the adapter list.
     */
    private void loadEnrolledEntrants() {
        Event event = EventViewModel.getEvent().getValue();
        if (event == null) {
            return;
        }

        ArrayList<String> enrolled = event.getAcceptedList();

        if (enrolled != null) {
            entrantList.clear();
            entrantList.addAll(enrolled);

            // Rebuild adapter so DAL loads new entrants
            adapter = new EntrantListAdapter(entrantList, requireContext());
            listView.setAdapter(adapter);
        }
    }

    /**
     * Sets up export CSV button.
     */
    private void setupCSVButton(View view) {
        Button exportCSV = view.findViewById(R.id.enrolled_export_button);

        exportCSV.setOnClickListener(v -> {
            Event event = EventViewModel.getEvent().getValue();
            if (event == null) return;

            ExportCSV exporter = new ExportCSV();
            exporter.exportCSVFile(requireContext(), event, "accepted_users.csv");
        });
    }
}