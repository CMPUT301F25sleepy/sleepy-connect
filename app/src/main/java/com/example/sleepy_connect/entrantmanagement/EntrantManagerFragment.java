package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sleepy_connect.CommunityFragment;
import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventListFragment;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.eventdetails.EventDetailsFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment class for navigating between an event's different entrant lists.
 * @author Sam Francisco
 */
public class EntrantManagerFragment extends Fragment implements EntrantManagerSelectedBottomSheet.EntrantManagerSelectedBottomSheetListener{
    private Event event;
    private final ArrayList<Entrant> entrantList = new ArrayList<>();

    public EntrantManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment EntrantManagerFragment.
     */
    public static EntrantManagerFragment newInstance(Event event) {
        EntrantManagerFragment fragment = new EntrantManagerFragment();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
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

        Bundle args = getArguments();

        event = (Event) args.getSerializable("event");

        //get the viewmodel for Lists
        // Get the shared ViewModel instance
        ListViewModel vm = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
        ArrayList<Entrant> entrantList = vm.getWaitingList().getValue();

        WaitlistFragment fragment = WaitlistFragment.newInstance(event);

        // set default child fragment to waitlist fragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.entrant_manager_fragment_container, fragment, null)
                .commit();

        // set click listener for list title
        ConstraintLayout list_title = view.findViewById(R.id.entrant_manager_cl_list);
        list_title.setOnClickListener(v -> {
            // open bottom sheet

            EntrantManagerBottomSheet bottomSheet = EntrantManagerBottomSheet.newInstance(event);
            bottomSheet.show(getChildFragmentManager(), "ModalBottomSheet");
        });
    }

    @Override
    public void EntrantManagerSelectedBottomSheetClosed(boolean bsClosed) {
        Log.d("Sucesss","WE HERE :D");
        if (bsClosed){

            EntrantManagerFragment fragment = EntrantManagerFragment.newInstance(event);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
