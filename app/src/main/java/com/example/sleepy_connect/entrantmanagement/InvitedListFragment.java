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
 * Will display the list of invited entrants for a specific event
 */
public class InvitedListFragment extends Fragment {

    private ListView listView;
    private ArrayList<Entrant> entrantList;
    private EntrantListAdapter adapter;

    public InvitedListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InvitedListFragment.
     */
    public static InvitedListFragment newInstance() {
        return new InvitedListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invited_list, container, false);

        listView = view.findViewById(R.id.invited_entrant_list);
        entrantList = new ArrayList<>();
        adapter = new EntrantListAdapter(entrantList, getContext());
        listView.setAdapter(adapter);

        return view;
    }
}