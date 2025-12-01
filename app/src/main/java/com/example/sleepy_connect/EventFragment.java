package com.example.sleepy_connect;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sleepy_connect.eventdetails.EventDetailsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * fragment class for the My Events pages on the nav bar
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    private String entrantID;

    private ListView WListView;
    private MyEventListAdapter Wadapter;
    private ArrayList<String> affiliatedEvents;
    private final List<Event> WEventList = new ArrayList<>();
    private EventDAL eventDAL;

    private Entrant user;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param entrantID ID number of entrant, for navigating to event details page.
     * @return A new instance of fragment TestFragment.
     */
    public static EventFragment newInstance(String entrantID) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString("entrant", entrantID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entrantID = getArguments().getString("entrant");

            // get user from viewmodel
            UserViewModel vmUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
            user = vmUser.getUser().getValue();
            assert user != null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        WListView = view.findViewById(R.id.waitlisted_events_list);
        //EListView = view.findViewById(R.id.enrolled_events_list);

        Wadapter = new MyEventListAdapter(WEventList);
        WListView.setAdapter(Wadapter);

        // get user from viewmodel
        UserViewModel vmUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        user = vmUser.getUser().getValue();
        assert user != null;

        affiliatedEvents = user.getAll_event_list();

        for (String event : affiliatedEvents) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            eventDAL = new EventDAL();

            eventDAL.getEvent(event, new EventDAL.OnEventRetrievedListener() {
                @Override
                public void onEventRetrieved(Event event) {
                    WEventList.add(event);
                    Wadapter.notifyDataSetChanged();
                }
            });

            WListView.setOnItemClickListener((parent, view1, position, id) -> {
                Event selectedEvent = WEventList.get(position);
                Log.d("EventFragment", "Clicked event: " + selectedEvent.getEventName());

                // pass selected event to viewmodel
                EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
                vmEvent.setEvent(selectedEvent);

                //set toolbar title
                TextView title = requireActivity().findViewById(R.id.set_title);
                title.setText("Event Details");

                //get entrant for vm model to get entrant id for require instance for event details
                UserViewModel vmEntrant = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                Entrant entrant = vmEntrant.getUser().getValue();

                EventDetailsFragment fragment = EventDetailsFragment.newInstance(entrant.getAndroid_id(), selectedEvent.getEventID());
                // open event details
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            });
        }

        if (WEventList.isEmpty()){
            Log.d("Empty", "WEventList is Empty");
        }
        return view;
    }

    /**
     * Custom adapter for the custom listview of events
     */
    public class MyEventListAdapter extends BaseAdapter {
        private final List<Event> events;

        public MyEventListAdapter(List<Event> events) {
            this.events = events;
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int position) {
            return events.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EventFragment.MyEventListAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.event_item_layout, parent, false);
                holder = new EventFragment.MyEventListAdapter.ViewHolder();
                holder.name = convertView.findViewById(R.id.event_title);
                holder.dates = convertView.findViewById(R.id.event_dates);
                holder.dayOfWeek = convertView.findViewById(R.id.event_dayOfWeek);
                holder.time = convertView.findViewById(R.id.event_times);
                convertView.setTag(holder);
            } else {
                holder = (EventFragment.MyEventListAdapter.ViewHolder) convertView.getTag();
            }

            Event event = events.get(position);
            holder.name.setText(event.getEventName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/y", Locale.getDefault());
            String start = dateFormat.format(new Date(event.getEventStartDate()));
            String end = dateFormat.format(new Date(event.getEventEndDate()));
            holder.dates.setText(start + " - " + end);
            holder.dayOfWeek.setText(event.getEventDayOfWeek());
            holder.time.setText(event.getEventTime());

            return convertView;
        }

        /**
         * class for the attributes changed by the holder
         */
        class ViewHolder {
            TextView name;
            TextView dates;
            TextView dayOfWeek;
            TextView time;
        }
    }
}

