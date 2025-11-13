package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sleepy_connect.R;

/**
 * UNIMPLEMENTED
 * Displays a list of waitlisted entrants for a specific event
 */
public class WaitlistFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_waitlist, container, false);
    }
}