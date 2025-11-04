package com.example.sleepy_connect.eventdetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sleepy_connect.R;

/**
 * Fragment class for creating new events
 * @author Sam Francisco
 */
public class CreateEventFragment extends Fragment {

    private static final String UID = "uid"; // to be updated
    private String uid;
    private boolean imageSet = false;
    private boolean geolocationOn = false;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uid Current user's id.
     * @return A new instance of fragment CreateEventFragment.
     */
    public static CreateEventFragment newInstance(String uid) {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle args = new Bundle();
        args.putString(UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve parameters
        if (getArguments() != null) {
            uid = getArguments().getString(UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initiate view listeners

        ConstraintLayout regStart = view.findViewById(R.id.edit_reg_start_button);
        regStart.setOnClickListener(v -> updateDateTime(v, R.id.edit_reg_start_date, R.id.edit_reg_start_time));

        ConstraintLayout regEnd = view.findViewById(R.id.edit_reg_end_button);
        regEnd.setOnClickListener(v -> updateDateTime(v, R.id.edit_reg_end_date, R.id.edit_reg_end_time));

        ConstraintLayout eventStart = view.findViewById(R.id.edit_event_start_button);
        eventStart.setOnClickListener(v -> updateDateTime(v, R.id.edit_event_start_date, R.id.edit_event_start_time));

        ConstraintLayout eventEnd = view.findViewById(R.id.edit_event_end_button);
        eventEnd.setOnClickListener(v -> updateDateTime(v, R.id.edit_event_end_date, R.id.edit_event_end_time));

        ImageView poster = view.findViewById(R.id.edit_event_poster);
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: get image

                // TODO: set imageSet if successful

            }
        });

        SwitchCompat geolocationSwitch = view.findViewById(R.id.edit_geolocation_switch);
        geolocationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // record state when changed
            geolocationOn = isChecked;
        });

        Button saveBtn = view.findViewById(R.id.event_confirm_edit_button);
        saveBtn.setOnClickListener(v -> {

            // link views
            EditText title = view.findViewById(R.id.edit_event_title);
            EditText eventCapacity = view.findViewById(R.id.edit_event_capacity_value);
            EditText waitlistCapacity = view.findViewById(R.id.edit_waitlist_capacity_value);
            EditText recCenter = view.findViewById(R.id.edit_host_rec_center_text);
            EditText address = view.findViewById(R.id.edit_host_address_text);
            EditText description = view.findViewById(R.id.edit_event_descr_text);
            TextView regStartDate = view.findViewById(R.id.edit_reg_start_date);
            TextView regStartTime = view.findViewById(R.id.edit_reg_start_time);
            TextView regEndDate = view.findViewById(R.id.edit_reg_end_date);
            TextView regEndTime = view.findViewById(R.id.edit_reg_end_time);
            TextView eventStartDate = view.findViewById(R.id.edit_event_start_date);
            TextView eventStartTime = view.findViewById(R.id.edit_event_start_time);
            TextView eventEndDate = view.findViewById(R.id.edit_event_end_date);
            TextView eventEndTime = view.findViewById(R.id.edit_event_end_time);

            // TODO: check if mandatory fields are set

            // TODO: unset mandatory fields -> highlight unset fields, click done

            // TODO: store data in database, click done

        });
    }

    public void updateDateTime(View view, int dateViewId, int timeViewId) {

        // TODO: open date time picker activity and get date and time

        // TODO: update event_end date and time
        TextView dateView = view.findViewById(dateViewId);
        TextView timeView = view.findViewById(timeViewId);

    }

    public boolean mandatoryFieldsFilled(EditText title, EditText eventCapacity, EditText recCenter, EditText address,
                                     TextView regStartDate, TextView regEndDate, TextView eventStartDate) {
        // TODO: logical or all the views' values' isEmpty
        return true;
    }

    public void highlightErrorFields(EditText title, EditText eventCapacity, EditText recCenter, EditText address,
                                     TextView regStartDate, TextView regEndDate, TextView eventStartDate) {
        // TODO: change all empty mandatory views' borders to red

        // TODO: show error message
    }
}