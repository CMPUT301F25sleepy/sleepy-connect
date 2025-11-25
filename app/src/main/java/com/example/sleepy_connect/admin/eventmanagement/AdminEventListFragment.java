package com.example.sleepy_connect.admin.eventmanagement;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Displays all events in the app
 */
public class AdminEventListFragment extends Fragment {

    private ArrayList<Event> events;
    private AdminEventListAdapter adapter;

    public AdminEventListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminEventListFragment.
     */
    public static AdminEventListFragment newInstance() {
        return new AdminEventListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // initialize list, listview, adapter
        events = new ArrayList<>();
        adapter = new AdminEventListAdapter(requireContext(), events);
        ListView listView = view.findViewById(R.id.admin_list_lv);
        listView.setAdapter(adapter);

        // initialize listener for event list item click
        listView.setOnItemClickListener((parent, view1, position, id) -> {

            // store selected event in viewmodel
            Event selectedEvent = events.get(position);
            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
            vmEvent.setEvent(selectedEvent);

            // show event details
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.admin_container, AdminEventDetailsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });

        // get all events
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    // add queried events to cleared events list
                    events.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Event event = doc.toObject(Event.class);
                        events.add(event);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}