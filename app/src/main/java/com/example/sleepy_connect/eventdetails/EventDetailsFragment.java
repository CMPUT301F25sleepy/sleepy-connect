package com.example.sleepy_connect.eventdetails;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sleepy_connect.CommunityCentre;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Fragment class for showing event details
 * @author Sam Francisco
 */
public class EventDetailsFragment extends Fragment {

    Event event;
    SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/y", Locale.getDefault());

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method for this fragment
     * @return A new instance of fragment EventDetailsFragment.
     */
    public static EventDetailsFragment newInstance(String param1, String param2) {
        return new EventDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // receive event details from viewmodel
        event = EventViewModel.getEvent().getValue();

        // set the fields from received event model
        setFields(view);

        // implement lottery button click showing lottery guidelines
        Button lotteryButton = view.findViewById(R.id.lottery_guidelines_button);
        lotteryButton.setOnClickListener(v -> {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.event_details_fragment_container, new LotteryGuidelinesFragment())
                    .commit();
        });
    }

    /**
     * Set each view's content depending on data received
     * @param view Fragment's root view
     */
    @SuppressLint("DefaultLocale")
    public void setFields(View view) {

        // set title
        TextView title = view.findViewById(R.id.event_title_display);
        title.setText(event.getEventName());

        // set location
        TextView location = view.findViewById(R.id.event_location);
        location.setText(formatLocation());

        // set registration date
        TextView regPeriod = view.findViewById(R.id.reg_deadline);
        regPeriod.setText(formatDatePeriod(event.getRegistrationOpens(), event.getRegistrationCloses()));

        // set event date
        TextView eventPeriod = view.findViewById(R.id.details_event_dates);
        eventPeriod.setText(formatDatePeriod(event.getEventStartDate(), event.getEventEndDate()));

        // set time
        TextView time = view.findViewById(R.id.event_time);
        time.setText(event.getEventTime());

        // set day of week
        TextView dayOfWeek = view.findViewById(R.id.dayOfWeek_label);
        dayOfWeek.setText(event.getEventDayOfWeek());

        // set description
        if (event.getDescription() != null) {
            TextView description = view.findViewById(R.id.event_description);
            description.setText(event.getDescription());
        }

        // set current waitlist size display
        TextView waitlistSize = view.findViewById(R.id.waitlist_count_display);
        waitlistSize.setText(String.format("There are %d people currently on the waitlist", event.getWaitlistSize()));

        // set poster if provided
        if (event.getPoster() != null) {
            ImageView poster = view.findViewById(R.id.event_details_poster);
            poster.setImageBitmap(event.getPoster().decodeImage());
        }
    }

    /**
     * Formats community centre info into one string to be displayed
     * @return Formatted community centre info string
     */
    public String formatLocation() {
        CommunityCentre recCenter = event.getCommunityCentre();
        return recCenter.getCommunityCentreName() + "\n" + recCenter.getCommunityCentreLocation();
    }

    /**
     * Formats a given date period string from the given start and end dates
     * @param start Start date
     * @param end End date
     * @return Formatted date period string
     */
    public String formatDatePeriod(long start, long end) {

        // format reg start and end dates
        String startStr = dateFormat.format(new Date(start));
        String endStr = dateFormat.format(new Date(end));
        return startStr + "-" + endStr;
    }
}