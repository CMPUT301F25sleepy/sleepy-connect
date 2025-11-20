package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
    private Event event;

    public InvitedListFragment() {
        // Required empty public constructor
    }

    public static InvitedListFragment newInstance(Event event) {
        InvitedListFragment fragment = new InvitedListFragment();
        Bundle args = new Bundle();
        args.putSerializable("event",event);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        event = EventViewModel.getEvent().getValue();

        View view = inflater.inflate(R.layout.fragment_invited_list, container, false);
        ListViewModel vm = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.invited_entrant_list);
        listView.setOnItemClickListener((parent, view1, position, id) -> {

            // retreive entrant from list
            String selectedEntrant = entrantList.get(position);

            // open bottom sheet
            EntrantManagerSelectedBottomSheet bottomSheet = EntrantManagerSelectedBottomSheet.newInstance("Invited");
            bottomSheet.show(getParentFragmentManager()  , "ModalBottomSheet"
            );});

    }
}