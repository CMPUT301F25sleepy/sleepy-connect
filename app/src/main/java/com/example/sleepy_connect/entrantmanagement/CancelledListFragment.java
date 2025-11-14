package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sleepy_connect.R;

/**
 * UNIMPLEMENTED
 * Will display the list of cancelled entrants for a specific event
 */
public class CancelledListFragment extends Fragment {

    public CancelledListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InvitedListFragment.
     */
    public static CancelledListFragment newInstance() {
        return new CancelledListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cancelled_list, container, false);
    }
}
