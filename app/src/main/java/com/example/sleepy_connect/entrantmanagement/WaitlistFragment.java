package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.R;

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
    public static WaitlistFragment newInstance() {
        return new WaitlistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waitlist, container, false);

        listView = view.findViewById(R.id.waitlist_entrant_list);
        entrantList = new ArrayList<>();
        adapter = new EntrantListAdapter(entrantList, getContext());
        listView.setAdapter(adapter);

        return view;
    }
}