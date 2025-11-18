package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sleepy_connect.R;

/**
 * Fragment class for navigating between an event's different entrant lists.
 * @author Sam Francisco
 */
public class EntrantManagerFragment extends Fragment {

    public EntrantManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of this fragment.
     * @return A new instance of fragment EntrantManagerFragment.
     */
    public static EntrantManagerFragment newInstance() {
        return new EntrantManagerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // change toolbar title
        TextView title = requireActivity().findViewById(R.id.set_title);
        title.setText("Manage Entrants");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entrant_manager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set default child fragment to waitlist fragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.entrant_manager_fragment_container, WaitlistFragment.class, null)
                .commit();

        // set click listener for list title
        ConstraintLayout list_title = view.findViewById(R.id.entrant_manager_cl_list);
        list_title.setOnClickListener(v -> {

            // open bottom sheet
            EntrantManagerBottomSheet bottomSheet = new EntrantManagerBottomSheet();
            bottomSheet.show(getChildFragmentManager(), "ModalBottomSheet");

        });
    }
}