package com.example.sleepy_connect.eventdetails;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
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
import com.example.sleepy_connect.ExportCSV;
import com.example.sleepy_connect.Image;
import com.example.sleepy_connect.InviteFromDetailsFragment;
import com.example.sleepy_connect.LocationHelper;
import com.example.sleepy_connect.Notification;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.ObtainGeolocation;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.SignUpFragment;
import com.example.sleepy_connect.UserViewModel;
import com.example.sleepy_connect.alertSelectFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
// Time picker inspired by Code with Cal
// https://www.youtube.com/watch?v=c6c1giRekB4
/**
 * Fragment class for showing event details
 * @author Sam Francisco
 */
public class EventDetailsFragment extends Fragment{

    Event event;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM. d, yyyy", Locale.getDefault());
    Entrant entrant;

    Boolean inInvited = false;
    Boolean inWaitlist = false;
    Boolean eventOpen = false;
    Button viewStatusButton;
    Button joinButton;
    Button leaveButton;
    public ArrayList<String> pendingList;
    public ArrayList<String> acceptedList;
    public ArrayList<String> waitList;

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

        joinButton = view.findViewById(R.id.waitlist_join_button);
        viewStatusButton = view.findViewById(R.id.view_status_button);
        leaveButton = view.findViewById(R.id.leave_waitlist_button);
        Bundle args = getArguments();
        String entrantID = args.getString("entrant");
        String eventID = args.getString("event");


        // get event from viewmodel
        EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        event = vmEvent.getEvent().getValue();
        assert event != null;

        // get user from viewmodel
        UserViewModel vmUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        entrant = vmUser.getUser().getValue();
        assert entrant != null;

        long currentDate = System.currentTimeMillis();
        if (currentDate > event.registrationOpens && currentDate < event.registrationCloses) {
            eventOpen = true;
        }

        //checks if user is in the waitlist and sets bool
        if (event.getWaitingList() != null) {
            waitList = event.getWaitingList();
            for (String entrant : waitList) {
                if (Objects.equals(entrant, entrantID) && currentDate < event.eventStartDate) {
                    inWaitlist = true;
                    break;
                }
            }
        }

        //checks if entrant has a withstanding invite for that event and sets bool
        if (event.getPendingList() != null) {
            pendingList = event.getPendingList();
            for (String entrant : pendingList) {
                if (Objects.equals(entrant, entrantID) && currentDate < event.eventStartDate) {
                    inInvited = true;
                    break;
                }
            }
        }

        //if the user is already enrolled, they will see no button other than lottery guidelines
        if (event.getAcceptedList() != null) {
            acceptedList = event.getAcceptedList();
            for (String entrant : acceptedList) {
                if (Objects.equals(entrant, entrantID) || !eventOpen) {
                    joinButton.setVisibility(View.GONE);
                }
            }
        }

        //if the user is on the invited list for that event
        if (inInvited) {
            joinButton.setVisibility(View.GONE);
            viewStatusButton.setVisibility(View.VISIBLE);
        }

        //if the entrant is already in the waitlist, it gives them the option to leave rather than join
        if(inWaitlist) {
            joinButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.VISIBLE);
        }

        // set the fields from received event model
        setFields(view);

        // implement lottery button click showing lottery guidelines
        Button lotteryButton = view.findViewById(R.id.lottery_guidelines_button);
        lotteryButton.setOnClickListener(v -> {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.event_details_fragment_container, new LotteryGuidelinesFragment())
                    .commit();
        });

        //allows entrant to leave the waitlist
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entrantID = entrant.getAndroid_id();
                UserViewModel vmUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                Entrant entrant = vmUser.getUser().getValue();
                if (waitList.contains(entrantID)) {
                    waitList.remove(entrantID);
                    entrant.removeFromAllEventList(eventID);
                }
                event.setWaitingList(waitList);

                DialogFragment SignFragment = SignUpFragment.newInstance();
                SignFragment.show(getParentFragmentManager(), "leave");

                // update vm model
                EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
                vmEvent.setEvent(event);

                // update database
                EventDAL db = new EventDAL();
                db.updateEvent(event);

                // update user and user model
                vmUser.setUser(entrant);
                EntrantDAL userdal = new EntrantDAL();
                userdal.updateEntrant(entrant);

                //restart fragment
                restartFragment();
            }
        });

        //opens fragment to accept or decline if they click the button to view their invitation
        viewStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment invitedFragment = com.example.sleepy_connect.InviteFromDetailsFragment.newInstance(eventID, entrantID);
                invitedFragment.show(getParentFragmentManager(), "invited");
            }
        });



        // implement join lottery click
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
                                        // Gets the users current location as they sign up
                                        LocationHelper.getUserLocation(requireActivity(), new LocationHelper.LocationCallback() {
                                            @Override
                                            public void onLocation(double lat, double lon) {

                                                // Save to event
                                                Map<String, Double> coord = new HashMap<>();
                                                coord.put("lat", lat);
                                                coord.put("lon", lon);

                                                if (event.getLocationsList() == null) {
                                                    event.setLocationsList(new ArrayList<>());
                                                }

                                                event.getLocationsList().add(coord);


                                                new EventDAL().updateEvent(event);

                                                Log.d("GEO", "Saved location: " + lat + ", " + lon);
                                            }

                                            @Override
                                            public void onError(String message) {
                                                Log.e("GEO", message);
                                            }
                                        });
                                        ArrayList<String> list = event.getWaitingList();
                                        if (list.contains(entrantID)){
                                            SignFragment.show(getParentFragmentManager(), "already applied");
                                        } else {
                                            event.addToWaitlist(entrantID);
                                            eDAL.updateEvent(event);
                                            SignFragment.show(getParentFragmentManager(), "success");
                                            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
                                            vmEvent.setEvent(event);

                                            //update necessary models and database
                                            entrant.addToAllEventList(eventID);
                                            DAL.updateEntrant(entrant);
                                            UserViewModel vmEntrant = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                                            vmEntrant.setUser(entrant);

                                            restartFragment();
                                        }
                                    } else {
                                        System.err.println("No event found with ID: " + eventID);
                                    }
                                }
                            });

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
        String endStr = dateFormat.format(new Date(event.getRegistrationCloses()));
        regPeriod.setText(endStr);

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
        waitlistSize.setText(String.valueOf(event.getWaitlistSize()));

        // set poster if provided
        if (event.getPoster() != null) {
            ImageView poster = view.findViewById(R.id.event_details_poster);
            poster.setBackground(null);
            Image img = new Image(event.getPoster());
            poster.setImageBitmap(img.decodeImage());
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
        return startStr + " - " + endStr;
    }

    public void restartFragment(){
        FragmentManager fm = requireActivity().getSupportFragmentManager();

        EventDetailsFragment fragment =
                EventDetailsFragment.newInstance(entrant.getAndroid_id(), event.getEventID());

        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }
}