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
import android.widget.Toast;

import com.example.sleepy_connect.CommunityFragment;
import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventListFragment;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.eventdetails.EventDetailsFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public static EntrantManagerFragment newInstance(Event event,String currentList) {
        EntrantManagerFragment fragment = new EntrantManagerFragment();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        args.putString("current", currentList);
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

        Fragment startFragment = null;
        String currentList = args.getString("current");

        if (currentList != null){
            if (currentList.equals("Waiting")){
                Log.d("idk", "Started waiting list");
                startFragment = WaitlistFragment.newInstance(event);
            } else if (currentList.equals("Enrolled")){
                Log.d("idk", "Started Enrolled list");
                startFragment = EnrolledListFragment.newInstance(event);
            } else if (currentList.equals("Invited")){
                Log.d("idk", "Started Invited list");
                startFragment = InvitedListFragment.newInstance(event);
            } else if (currentList.equals("Cancelled")) {
                Log.d("idk", "Started Cancelled list");
                startFragment = CancelledListFragment.newInstance(event);
            }
        } else {
                startFragment = WaitlistFragment.newInstance(event);
        }

        // set default child fragment to waitlist fragment
        assert startFragment != null;
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.entrant_manager_fragment_container, startFragment, null)
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
    public void EntrantManagerSelectedBottomSheetClosed(boolean bsClosed,String label,Event updateEvent) {
        if (bsClosed){
            this.event = updateEvent;

            Fragment currentfragment = null;
            if (label == "Waiting"){
                currentfragment = WaitlistFragment.newInstance(updateEvent);
                Toast.makeText(requireContext(), "Entrant Deleted from Waitlist", Toast.LENGTH_SHORT).show();
            } else if (label == "Invited") {
                currentfragment = InvitedListFragment.newInstance(updateEvent);
                Toast.makeText(requireContext(), "Entrant Deleted from Invited List", Toast.LENGTH_SHORT).show();
            } else if (label == "Cancelled") {
                currentfragment = CancelledListFragment.newInstance(updateEvent);
                Toast.makeText(requireContext(), "Entrant Deleted from Cancelled List", Toast.LENGTH_SHORT).show();
            } else if (label == "Enrolled") {
                currentfragment = EnrolledListFragment.newInstance(updateEvent);
                Toast.makeText(requireContext(), "Entrant Deleted from Enrolled List", Toast.LENGTH_SHORT).show();
            }

            assert currentfragment != null;
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.entrant_manager_fragment_container, currentfragment, null)
                    .commit();
        }
    }
}
