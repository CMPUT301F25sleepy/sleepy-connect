package com.example.sleepy_connect.admin.eventmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 *custom list adapter class for the event list in the admin view
 */
public class AdminEventListAdapter extends ArrayAdapter<Event> {

    public AdminEventListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // inflate view
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_item_layout, parent, false);
        }

        // get event data
        Event event = getItem(position);
        assert event != null;

        // set event name
        TextView tvName = view.findViewById(R.id.event_title);
        tvName.setText(event.getEventName());

        // set event duration
        TextView tvDuration = view.findViewById(R.id.event_dates);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/y", Locale.getDefault());
        String start = dateFormat.format(new Date(event.getEventStartDate()));
        String end = dateFormat.format(new Date(event.getEventEndDate()));
        String duration = start + " - " + end;
        tvDuration.setText(duration);

        // set event day of week
        TextView tvDayOfWeek = view.findViewById(R.id.event_dayOfWeek);
        tvDayOfWeek.setText(event.getEventDayOfWeek());

        // set event time
        TextView tvTime = view.findViewById(R.id.event_times);
        tvTime.setText(event.getEventTime());

        return view;
    }
}
