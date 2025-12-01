package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
    private Event event;

    public EnrolledListFragment() {
        // Required empty constructor
    }

    public static EnrolledListFragment newInstance(Event event) {
        EnrolledListFragment fragment = new EnrolledListFragment();
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

        View view = inflater.inflate(R.layout.fragment_enrolled_list, container, false);
        ListViewModel vm = new ViewModelProvider(requireActivity()).get(ListViewModel.class);

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

        // get event from viewmodel
        EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        event = vmEvent.getEvent().getValue();
        assert event != null;

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

            // get event from viewmodel
            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
            event = vmEvent.getEvent().getValue();
            assert event != null;

            if (event == null) return;

            ExportCSV exporter = new ExportCSV();
            exporter.exportCSVFile(requireContext(), event, "accepted_users.csv");
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.enrolled_entrant_list);
        listView.setOnItemClickListener((parent, view1, position, id) -> {

            // retrieve entrant from list
            String selectedEntrant = entrantList.get(position);

            // open bottom sheet
            EntrantManagerSelectedBottomSheet bottomSheet = EntrantManagerSelectedBottomSheet.newInstance("Enrolled", selectedEntrant);
            bottomSheet.show(getParentFragmentManager()  , "ModalBottomSheet");
        });

    }
}