package com.example.sleepy_connect.admin.postermanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.admin.profilemanagement.AdminProfileListAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Displays all posters in the app
 */
public class AdminPosterListFragment extends Fragment {

    private ArrayList<Event> events;

    public AdminPosterListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminPosterListFragment.
     */
    public static AdminPosterListFragment newInstance() {
        return new AdminPosterListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize list, listview, adapter
        events = new ArrayList<>();
        AdminPosterListAdapter adapter = new AdminPosterListAdapter(requireContext(), events);
        ListView listView = view.findViewById(R.id.admin_list_lv);
        listView.setAdapter(adapter);

        // get posters
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .whereNotEqualTo("poster", null)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    // add all queried entrants to cleared entrant list
                    events.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Event event = doc.toObject(Event.class);
                        events.add(event);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}