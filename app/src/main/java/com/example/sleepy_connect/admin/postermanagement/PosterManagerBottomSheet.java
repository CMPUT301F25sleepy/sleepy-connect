package com.example.sleepy_connect.admin.postermanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Dialog fragment class for removing posters
 * @author Sam Francisco
 */
public class PosterManagerBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the bottom sheet layout
        View view = inflater.inflate(R.layout.bottom_sheet_poster_manager, container, false);

        // set listener for removing selected poster
        TextView tvRemovePoster = view.findViewById(R.id.bs_poster_manager_tv_remove_poster);
        tvRemovePoster.setOnClickListener(v -> {

            // get event from viewmodel
            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
            Event event = vmEvent.getEvent().getValue();
            assert event != null;

            // clear event poster
            event.setPoster(null);

            // update db
            EventDAL eventDAL = new EventDAL();
            eventDAL.updateEvent(event);

            // update list
            AdminPosterListFragment parent = (AdminPosterListFragment) requireParentFragment();
            parent.getPosters();

            // close bottom sheet
            dismiss();
        });

        return view;
    }
}