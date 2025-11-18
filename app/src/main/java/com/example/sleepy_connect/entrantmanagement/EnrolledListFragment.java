package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.R;

import java.util.ArrayList;

/**
 * UNIMPLEMENTED
 * Will display the list of enrolled entrants for a specific event
 */
public class EnrolledListFragment extends Fragment {

    private ListView listView;
    private ArrayList<Entrant> entrantList;
    private EntrantListAdapter adapter;

    public EnrolledListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InvitedListFragment.
     */
    public static EnrolledListFragment newInstance() {
        return new EnrolledListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enrolled_list, container, false);

        listView = view.findViewById(R.id.enrolled_entrant_list);
        entrantList = new ArrayList<>();
        adapter = new EntrantListAdapter(entrantList, getContext());
        listView.setAdapter(adapter);

        return view;
    }
}
