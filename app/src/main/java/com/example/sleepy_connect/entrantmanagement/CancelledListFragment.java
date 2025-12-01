package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private Event event;

    public CancelledListFragment() {
        // Required empty public constructor
    }

    public static CancelledListFragment newInstance(Event event) {
        CancelledListFragment fragment = new CancelledListFragment();
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

        // get event from viewmodel
        EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        event = vmEvent.getEvent().getValue();
        assert event != null;

        ArrayList<String> cancelled = event.getDeclinedList();

        if (cancelled != null) {
            entrantList.clear();
            entrantList.addAll(cancelled);

            // Rebuild adapter
            adapter = new EntrantListAdapter(entrantList, requireContext());
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.cancelled_entrant_list);
        listView.setOnItemClickListener((parent, view1, position, id) -> {

            // retrieve entrant from list
            String selectedEntrant = entrantList.get(position);

            // open bottom sheet
            EntrantManagerSelectedBottomSheet bottomSheet = EntrantManagerSelectedBottomSheet.newInstance("Cancelled", selectedEntrant);
            bottomSheet.show(getParentFragmentManager()  , "ModalBottomSheet");
        });

    }
}