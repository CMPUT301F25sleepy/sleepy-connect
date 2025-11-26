package com.example.sleepy_connect.admin.eventmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sleepy_connect.CommunityCentre;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.Image;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.admin.AdminEventViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Displays event details and provides admin event management actions.
 */
public class AdminEventDetailsFragment extends Fragment {

    private Event event;
    private TextView listLabel;

    public AdminEventDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminEventDetails.
     */
    public static AdminEventDetailsFragment newInstance() {
        return new AdminEventDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // hide list label
        listLabel = requireActivity().findViewById(R.id.admin_tv_list_label);
        listLabel.setVisibility(View.GONE);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_event_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get selected event from viewmodel
        AdminEventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(AdminEventViewModel.class);
        event = vmEvent.getEvent().getValue();
        assert event != null;

        // display data
        setFields(view);

        // initialize click listener for delete button
        TextView tvDelete = view.findViewById(R.id.admin_event_details_tv_delete);
        tvDelete.setOnClickListener(v -> {
            EventDAL eventDAL = new EventDAL();
            eventDAL.removeEvent(event.getEventID(), event.getCreatorID(), this::finishProcedure);
        });

        // initialize click listener for return button
        TextView tvReturn = view.findViewById(R.id.admin_event_details_tv_return);
        tvReturn.setOnClickListener(v -> finishProcedure());
    }

    /**
     * Displays event data.
     * @param root Fragment's root view.
     */
    private void setFields(View root) {

        // set poster
        String posterString = event.getPoster();
        if (posterString != null) {
            ImageView ivPoster = root.findViewById(R.id.admin_event_details_iv_poster);
            Image poster = new Image(posterString);
            ivPoster.setImageBitmap(poster.decodeImage());
        }

        // set title
        TextView tvTitle = root.findViewById(R.id.admin_event_details_tv_title);
        tvTitle.setText(event.getEventName());

        // set duration
        TextView tvDuration = root.findViewById(R.id.admin_event_details_tv_event_period);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/y", Locale.getDefault());
        String start = dateFormat.format(new Date(event.getEventStartDate()));
        String end = dateFormat.format(new Date(event.getEventEndDate()));
        String duration = start + " - " + end + ", " + event.getEventTime();
        tvDuration.setText(duration);

        // set community centre and address
        CommunityCentre location = event.getCommunityCentre();
        TextView tvLocation = root.findViewById(R.id.admin_event_details_tv_location);
        TextView tvAddress = root.findViewById(R.id.admin_event_details_tv_address);
        tvLocation.setText(location.getCommunityCentreName());
        tvAddress.setText(location.getCommunityCentreLocation());

        // set reg period
        TextView tvRegPeriod = root.findViewById(R.id.admin_event_details_tv_reg_period);
        String regStart = dateFormat.format(new Date(event.getRegistrationOpens()));
        String regEnd = dateFormat.format(new Date(event.getRegistrationCloses()));
        String regPeriod = regStart + " - " + regEnd;
        tvRegPeriod.setText(regPeriod);

        // set description
        String description = event.getDescription();
        if (description != null) {
            TextView tvDescription = root.findViewById(R.id.admin_event_details_tv_description);
            tvDescription.setText(description);
        }

    }

    /**
     * Shows activity's list label and pops fragment off backstack.
     */
    private void finishProcedure() {

        listLabel.setVisibility(View.VISIBLE);
        requireActivity().getSupportFragmentManager()
                .popBackStack();

    }
}