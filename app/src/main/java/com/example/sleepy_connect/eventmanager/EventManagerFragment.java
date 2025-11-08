package com.example.sleepy_connect.eventmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.UserViewModel;
import com.example.sleepy_connect.eventdetails.CreateEventFragment;
import com.example.sleepy_connect.eventdetails.EventViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventManagerFragment extends Fragment {

    private ListView listview;
    private EventManagerListAdapter adapter;
    private Entrant user;
    private final List<Event> eventList = new ArrayList<>();

    public EventManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EventMangerFragment.
     */
    public static EventManagerFragment newInstance() {
        return new EventManagerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_manager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set click listener for create event
        Button newEventButton = view.findViewById(R.id.new_event_button);
        newEventButton.setOnClickListener(v-> openCreateEvent());

        // initialize listview and adapter
        listview = view.findViewById(R.id.event_manager_lv_events);
        adapter = new EventManagerListAdapter(eventList);
        listview.setAdapter(adapter);

        // get user info
        user = UserViewModel.getUser().getValue();

        // fetch and display events created by the user
        fetchEventsCreated();

        // set click listener for list view item
        listview.setOnItemClickListener((parent, view1, position, id) -> {
            Event selectedEvent = eventList.get(position);

            // pass selected event to viewmodel
            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
            vmEvent.setEvent(selectedEvent);

            // open bottom sheet
            EventManagerBottomSheet bottomSheet = new EventManagerBottomSheet();
            bottomSheet.show(requireActivity().getSupportFragmentManager(), "ModalBottomSheet");
        });
    }

    private void openCreateEvent() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, CreateEventFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public void fetchEventsCreated() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the community centre
        db.collection("events")
                .whereEqualTo("creatorID", user.getAndroid_id())
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    // add queried events to cleared event list
                    eventList.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        eventList.add(doc.toObject(Event.class));
                    }
                    adapter.notifyDataSetChanged();

                });
    }

    private class EventManagerListAdapter extends BaseAdapter {
        private final List<Event> events;

        public EventManagerListAdapter(List<Event> events) {
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
            EventManagerFragment.EventManagerListAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.event_item_layout, parent, false);
                holder = new EventManagerFragment.EventManagerListAdapter.ViewHolder();
                holder.name = convertView.findViewById(R.id.event_title);
                holder.dates = convertView.findViewById(R.id.event_dates);
                holder.dayOfWeek = convertView.findViewById(R.id.event_dayOfWeek);
                holder.time = convertView.findViewById(R.id.event_times);
                convertView.setTag(holder);
            } else {
                holder = (EventManagerFragment.EventManagerListAdapter.ViewHolder) convertView.getTag();
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

        class ViewHolder {
            TextView name;
            TextView dates;
            TextView dayOfWeek;
            TextView time;
        }
    }
}
