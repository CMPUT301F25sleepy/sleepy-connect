package com.example.sleepy_connect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sleepy_connect.eventdetails.EventDetailsFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment that displays the list of events for a particular location
 */
public class EventListFragment extends Fragment {
    private static final String ARG_LOCNAME = "locationName";

    private String locationName;
    private ListView listView;
    private EventListAdapter adapter;
    private final List<Event> eventList = new ArrayList<>();

    private EventDAL eventDAL;

    public EventListFragment() {
        // Required empty public constructor
    }

    public static EventListFragment newInstance(String locationName,String entrantID) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCNAME, locationName);
        args.putString("entrant", entrantID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventDAL = new EventDAL();
        if (getArguments() != null) {
            locationName = getArguments().getString(ARG_LOCNAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_display, container, false);

        listView = view.findViewById(R.id.event_list_view);
        adapter = new EventListAdapter(eventList);
        listView.setAdapter(adapter);

        Bundle args = getArguments();
        String entrantID = args.getString("entrant");

        TextView eventLocation = view.findViewById(R.id.location);
        if (locationName != null) {
            eventLocation.setText(locationName);
        } else {
            Log.e("EventListFragment", "Location name null");
        }

        // Fetch and display events for this community centre
        fetchEventsForLocation(locationName);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Event selectedEvent = eventList.get(position);
            Log.d("EventListFragment", "Clicked event: " + selectedEvent.getEventName());

            // pass selected event to viewmodel
            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
            vmEvent.setEvent(selectedEvent);

            //set toolbar title
            TextView title = requireActivity().findViewById(R.id.set_title);
            title.setText("Event Details");

            // open event details
            EventDetailsFragment fragment = EventDetailsFragment.newInstance(entrantID, selectedEvent.getEventID());

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    /**
     * Accesses the database to get all valid events
     * @param locationName string of name which is searched for in events
     */
    private void fetchEventsForLocation(String locationName) {
        /*Gets all events for a rec centre if you give the name*/
        if (locationName == null || locationName.isEmpty()) {
            Log.e("EventListFragment", "cant get events when null");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the community centre
        db.collection("community centres").document(locationName)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Log.e("EventListFragment", "Community centre broke)");
                        return;
                    }

                    List<String> eventIDs = (List<String>) documentSnapshot.get("events");
                    if (eventIDs == null || eventIDs.isEmpty()) {
                        Log.d("EventListFragment", "No events in " + locationName);
                        return;
                    }

                    Log.d("EventListFragment", "got " + eventIDs.size() + " event ids in " + locationName);

                    // get event by ID
                    eventList.clear();
                    for (String id : eventIDs) {
                        db.collection("events").document(id)
                                .get()
                                .addOnSuccessListener(eventDoc -> {
                                    if (eventDoc.exists()) {
                                        Event event = eventDoc.toObject(Event.class);
                                        eventList.add(event);
                                        adapter.notifyDataSetChanged();
                                        Log.d("EventListFragment", "Got event" + event.getEventName());
                                    }
                                })
                                .addOnFailureListener(e ->
                                        Log.e("EventListFragment", "Something broke with event"));
                    }
                })
                .addOnFailureListener(e ->
                        Log.e("EventListFragment", "Something broke"));
    }

    /**
     * Custom adapter for the custom listview of events
     */
    public class EventListAdapter extends BaseAdapter {
        private final List<Event> events;

        public EventListAdapter(List<Event> events) {
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.event_item_layout, parent, false);
                holder = new ViewHolder();
                holder.name = convertView.findViewById(R.id.event_title);
                holder.dates = convertView.findViewById(R.id.event_dates);
                holder.dayOfWeek = convertView.findViewById(R.id.event_dayOfWeek);
                holder.time = convertView.findViewById(R.id.event_times);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
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
