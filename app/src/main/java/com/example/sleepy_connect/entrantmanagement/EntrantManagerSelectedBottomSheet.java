package com.example.sleepy_connect.entrantmanagement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.UserViewModel;
import com.example.sleepy_connect.eventmanager.EventManagerBottomSheet;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntrantManagerSelectedBottomSheet extends BottomSheetDialogFragment {
    private String entrantID;
    private Event event;
    private String listname;
    private EventViewModel vmEvent;

    public interface EntrantManagerSelectedBottomSheetListener {
        void EntrantManagerSelectedBottomSheetClosed(boolean bsClosed, String label, Event event);
    }

    private EntrantManagerSelectedBottomSheetListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment parent = getParentFragment();
        if (parent instanceof EntrantManagerSelectedBottomSheetListener) {
            listener = (EntrantManagerSelectedBottomSheetListener) parent;
        } else {
            throw new RuntimeException(parent + " must implement EntrantManagerSelectedBottomSheetListener");
        }
    }

    public static EntrantManagerSelectedBottomSheet newInstance(String listname, String entrantID) {
        EntrantManagerSelectedBottomSheet fragment = new EntrantManagerSelectedBottomSheet();
        Bundle args = new Bundle();
        args.putString("list", listname);
        args.putString("entrantID", entrantID);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        listname = args.getString("list");
        entrantID = args.getString("entrantID");

        // get event from viewmodel
        EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        event = vmEvent.getEvent().getValue();
        assert event != null;

        // get user from viewmodel and its id
        UserViewModel vmUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        Entrant user = vmUser.getUser().getValue();

        // Inflate the bottom sheet layout
        View view = inflater.inflate(R.layout.fragment_entrant_manager_selected_sheet, container, false);
        TextView delete_entrant = view.findViewById(R.id.bs_entrant_manager_selected_delete_entrant);

        delete_entrant.setOnClickListener( v -> {
            deleteEntrantPressed();
            dismiss();
        });

        return view;
    }

    public void deleteEntrantPressed() {
        EventDAL db = new EventDAL();
        EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        // following if statements determines which list to modify and delete the corresponding entrant
        if (Objects.equals(listname, "Waiting")) {
            ArrayList<String> list = event.getWaitingList();
            if (list.contains(entrantID)) {
                list.remove(entrantID);
                event.setWaitingList(list);
                db.updateEvent(event);
                vmEvent.setEvent(event);
                listener.EntrantManagerSelectedBottomSheetClosed(true, "Waiting", event);
            } else {
                Log.d("Remove from list fail", "Could not remove " + entrantID + " from waiting list.");
            }
        } else if (Objects.equals(listname, "Invited")) {
            ArrayList<String> list = event.getPendingList();
            if (list.contains(entrantID)) {
                list.remove(entrantID);
                event.setPendingList(list);
                db.updateEvent(event);
                vmEvent.setEvent(event);
                listener.EntrantManagerSelectedBottomSheetClosed(true, "Invited", event);
            } else {
                Log.d("Remove from list fail", "Could not remove " + entrantID + " from pending list.");
            }
        } else if (Objects.equals(listname, "Enrolled")) {
            ArrayList<String> list = event.getAcceptedList();
            Log.d("DEBUG", "EntrantID to remove: " + entrantID);
            Log.d("DEBUG", "Current accepted list: " + list.toString());
            Log.d("DEBUG", "Pending list: " + event.getPendingList());
            Log.d("DEBUG", "Waiting list: " + event.getWaitingList());
            if (list.contains(entrantID)) {
                list.remove(entrantID);
                event.setAcceptedList(list);
                db.updateEvent(event);
                vmEvent.setEvent(event);
                listener.EntrantManagerSelectedBottomSheetClosed(true, "Enrolled", event);
            } else {
                Log.d("Remove from list fail", "Could not remove " + entrantID + " from enroll list.");
            }
        } else if (Objects.equals(listname, "Cancelled")) {
            ArrayList<String> list = event.getDeclinedList();
            if (list.contains(entrantID)) {
                list.remove(entrantID);
                event.setDeclinedList(list);
                db.updateEvent(event);
                vmEvent.setEvent(event);
                listener.EntrantManagerSelectedBottomSheetClosed(true, "Cancelled", event);
            } else {
                Log.d("Remove from list fail", "Could not remove " + entrantID + " from decline list.");
            }
        }
    }
}
