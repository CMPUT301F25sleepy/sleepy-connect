package com.example.sleepy_connect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends Fragment {
    private static final String ARG_LOCNAME = "locationName";

    private String locationName;

    private ListView listView;
    private EventListFragment.EventListAdapter adapter;
    private final List<Event> eventList = new ArrayList<>();

    public EventListFragment() {
        // Required empty public constructor
    }

    public static EventListFragment newInstance(String locationName) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCNAME, locationName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locationName = getArguments().getString(ARG_LOCNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_display, container, false);

        listView =view.findViewById(R.id.event_list_view);
        adapter = new EventListFragment.EventListAdapter(eventList);
        listView.setAdapter(adapter);

        if (getArguments() != null) {
            String location = getArguments().getString(ARG_LOCNAME);
            TextView event_location = view.findViewById(R.id.location);
            event_location.setText(location);
        }

        listView.setOnItemClickListener((parent, view1, position, id) -> {

            //openEventDetails();
        });

        return view;
    }

    //TODO-Change to open specific event details
    /*private void openEventDetails() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, MyEventFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }*/



    // Adapter for Amelia's Fancy ListView
    private class EventListAdapter extends BaseAdapter {

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
            EventListFragment.EventListAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.event_item_layout, parent, false);
                holder = new EventListFragment.EventListAdapter.ViewHolder();
                holder.name = convertView.findViewById(R.id.event_title);
                holder.dates = convertView.findViewById(R.id.event_dates);
                holder.dayOfWeek = convertView.findViewById(R.id.event_dayOfWeek);
                holder.time = convertView.findViewById((R.id.event_times));
                convertView.setTag(holder);
            } else {
                holder = (EventListFragment.EventListAdapter.ViewHolder) convertView.getTag();
            }

            Event event = events.get(position);
            holder.name.setText(event.getEventName());
            holder.dates.setText("startDate-endDate");
            holder.dayOfWeek.setText(event.getEventDayOfWeek());
            holder.time.setText(event.getEventTime());

            return convertView;
        }

        class ViewHolder {
            TextView name;
            TextView dates;
            TextView dayOfWeek;
            TextView time;
        }
    }
}

