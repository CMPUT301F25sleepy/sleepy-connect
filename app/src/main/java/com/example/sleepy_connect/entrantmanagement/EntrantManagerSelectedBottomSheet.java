package com.example.sleepy_connect.entrantmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.eventmanager.EventManagerBottomSheet;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EntrantManagerSelectedBottomSheet extends BottomSheetDialogFragment {

    public static EntrantManagerSelectedBottomSheet newInstance(Event event) {
        EntrantManagerSelectedBottomSheet fragment = new EntrantManagerSelectedBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the bottom sheet layout
        View view = inflater.inflate(R.layout.fragment_entrant_manager_selected_sheet, container, false);
        return view;
    }

    public void deleteEntrantPressed(View view){
        // TODO implement deleting entrant
    }

    public void selectNotifPressed(View view){
        // TODO implement sending notification to entrant
    }


}
