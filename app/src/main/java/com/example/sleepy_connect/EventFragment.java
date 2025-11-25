package com.example.sleepy_connect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sleepy_connect.eventdetails.EventDetailsFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * DOES NOT CURRENTLY WORK
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView WListView;
    private MyEventListAdapter Wadapter;
    private ArrayList<String> affiliatedEvents;

    private ListView EListView;
    private MyEventListAdapter Eadapter;

    //private EventListFragment.EventListAdapter adapter;
    private final List<Event> WEventList = new ArrayList<>();
    //private final List<String> EEventList = new ArrayList<>();

    private EventDAL eventDAL;

    private Entrant user;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

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
            //Eadapter = new MyEventListAdapter(EEventList);
            //EListView.setAdapter(Eadapter);

            //TODO - differentiate between current events and all events


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

        /*EListView.setOnItemClickListener((parent, view1, position, id) -> {
            Event selectedEvent = EEventList.get(position);
            Log.d("EventListFragment", "Clicked event: " + selectedEvent.getEventName());

            // pass selected event to viewmodel
            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
            vmEvent.setEvent(selectedEvent);

            //set toolbar title
            TextView title = requireActivity().findViewById(R.id.set_title);
            title.setText("Event Details");

            // open event details
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, EventDetailsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });*/


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