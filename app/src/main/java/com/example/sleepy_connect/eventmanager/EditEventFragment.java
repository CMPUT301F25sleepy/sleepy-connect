package com.example.sleepy_connect.eventmanager;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.Image;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.EventViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEventFragment extends Fragment {

    Event event;
    Image poster;
    Boolean geolocationOn;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d, y", Locale.getDefault());

    // views
    EditText etTitle;
    TextView tvRegStart;
    TextView tvRegEnd;
    TextView tvEventStart;
    TextView tvEventEnd;
    EditText etStartTime;
    EditText etEndTime;
    EditText etEventCapacity;
    EditText etWaitlistCapacity;
    EditText etLocationName;
    EditText etLocationAddress;
    SwitchCompat switchGeolocation;
    EditText etDescription;
    ImageView ivPoster;

    public EditEventFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method for this fragment.
     * @return A new instance of fragment EditEventFragment.
     */
    public static EditEventFragment newInstance() {
        return new EditEventFragment();
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

        // receive event details from viewmodel
        event = EventViewModel.getEvent().getValue();

        // set the fields from received event model
        initViewReferences(view);
        setFields(view);
    }

    /**
     * Link the fragment's views
     * @param view Fragment's root view
     */
    public void initViewReferences(View view) {
        EditText etTitle = view.findViewById(R.id.edit_event_title);
        TextView tvRegStart = view.findViewById(R.id.edit_reg_start_date);
        TextView tvRegEnd = view.findViewById(R.id.edit_reg_end_date);
        TextView tvEventStart = view.findViewById(R.id.edit_event_start_date);
        TextView tvEventEnd = view.findViewById(R.id.edit_event_end_date);
        EditText etStartTime = view.findViewById(R.id.edit_event_time_start_time);
        EditText etEndTime = view.findViewById(R.id.edit_event_time_end_time);
        EditText etEventCapacity = view.findViewById(R.id.edit_event_capacity_value);
        EditText etWaitlistCapacity = view.findViewById(R.id.edit_waitlist_capacity_value);
        EditText etLocationName = view.findViewById(R.id.edit_host_rec_center_text);
        EditText etLocationAddress = view.findViewById(R.id.edit_host_address_text);
        SwitchCompat switchGeolocation = view.findViewById(R.id.edit_geolocation_switch);
        EditText etDescription = view.findViewById(R.id.edit_event_descr_text);
        ImageView ivPoster = view.findViewById(R.id.edit_event_poster);
    }

    /**
     * Set fields to initial event data
     * @param view Fragment's root view
     */
    @SuppressLint("DefaultLocale")
    public void setFields(View view) {

        etTitle.setText(event.getEventName());
        tvRegStart.setText(dateFormat.format(new Date(event.getRegistrationOpens())));
        tvRegEnd.setText(dateFormat.format(new Date(event.getRegistrationCloses())));
        tvEventStart.setText(dateFormat.format(new Date(event.getEventStartDate())));
        tvEventEnd.setText(dateFormat.format(new Date(event.getEventEndDate())));
        etEventCapacity.setText(String.format("%d", event.getEventCapacity()));
        etLocationName.setText(event.getCommunityCentre().getCommunityCentreName());
        etLocationAddress.setText(event.getCommunityCentre().getCommunityCentreLocation());

        // get time
        String[] times = event.getEventTime().split("-");
        etStartTime.setText(times[0].strip());
        etEndTime.setText(times[1].strip());

        // get geolocation
        switchGeolocation.setChecked(event.isGeolocationEnabled());
        geolocationOn = event.isGeolocationEnabled();

        // get waitlist capacity
        if (event.getWaitlistCapacity() != Integer.MAX_VALUE) {
            etWaitlistCapacity.setText(String.format("%d", event.getWaitlistCapacity()));
        }

        // set description
        if (event.getDescription() != null) {
            etDescription.setText(event.getDescription());
        }

        // set poster if provided
        if (event.getPoster() != null) {
            ivPoster.setImageBitmap(event.getPoster().decodeImage());
        }
    }
}