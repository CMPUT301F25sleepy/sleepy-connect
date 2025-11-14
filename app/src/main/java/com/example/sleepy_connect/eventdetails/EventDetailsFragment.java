package com.example.sleepy_connect.eventdetails;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sleepy_connect.AlertFragment;
import com.example.sleepy_connect.CommunityCentre;
import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.EntrantDAL;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.Notification;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.SignUpFragment;
import com.example.sleepy_connect.alertSelectFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Fragment class for showing event details
 * @author Sam Francisco
 */
public class EventDetailsFragment extends Fragment{

    Event event;
    SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/y", Locale.getDefault());

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method for this fragment
     * @return A new instance of fragment EventDetailsFragment.
     */
    public static EventDetailsFragment newInstance(String entrantID, String eventID) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString("entrant",entrantID);
        args.putString("event",eventID);
        fragment.setArguments(args);
        return fragment;
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

        // implement join lottery click
        Bundle args = getArguments();
        String entrantID = args.getString("entrant");
        String eventID = args.getString("event");
        Button joinButton = view.findViewById(R.id.waitlist_join_button);
        joinButton.setOnClickListener(v -> {

            // check if user has filled out all details
            EntrantDAL DAL = new EntrantDAL();
            DAL.getEntrant(entrantID, new EntrantDAL.OnEntrantRetrievedListener() {
                @Override
                public void onEntrantRetrieved(Entrant entrant) {
                    if (entrant != null) {
                        // obtain all info from user
                        String user = entrant.getUsername();
                        String first = entrant.getFirst_name();
                        String last = entrant.getLast_name();
                        String birthday = entrant.getBirthday();
                        String phone = entrant.getPhone_number();
                        String email = entrant.getEmail();
                        DialogFragment SignFragment = SignUpFragment.newInstance();

                        if (user == null || first == null || last == null || birthday == null || phone == null || email == null || user == "" || first == "" || last == "" || birthday == "" || phone == "" || email == "") {
                            // if any information is missing, pop up a sign up fragment to fill details
                            SignFragment.show(getParentFragmentManager(), "failure");
                        } else {
                            // If user has everything filled out, sign user to waitlist
                            EventDAL eDAL = new EventDAL();
                            eDAL.getEvent(eventID, new EventDAL.OnEventRetrievedListener() {
                                @Override
                                public void onEventRetrieved(Event event){
                                    if (event != null){
                                        ArrayList<String> list = event.getWaitingList();
                                        if (list.contains(entrantID)){
                                            SignFragment.show(getParentFragmentManager(), "already applied");
                                        } else {
                                            event.addToWaitlist(entrantID);
                                            eDAL.updateEvent(event);
                                            SignFragment.show(getParentFragmentManager(), "success");
                                        }
                                    } else {
                                        System.err.println("No event found with ID: " + eventID);
                                    }
                                }
                            });
                            entrant.addToAllEventList(eventID);
                            DAL.updateEntrant(entrant);

                        }
                    } else {
                        System.err.println("No entrant found with ID: " + entrantID);
                    }
                }
            });
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