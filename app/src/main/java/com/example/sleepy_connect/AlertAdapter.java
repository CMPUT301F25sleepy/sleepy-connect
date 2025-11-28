package com.example.sleepy_connect;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Custom array adapter for the notifications from events
 */
public class AlertAdapter extends ArrayAdapter<Notification> {
    /* Sets up notification in the list for the list view */

    public AlertAdapter(@NonNull Context context, ArrayList<Notification> array) {
        super(context,0,array);
    }

    @NonNull
    @Override
    //function sets up custom element for listView in History
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.alert_layout,parent,false);
        }
        else {
            view = convertView;
        }

        Notification notif = getItem(position);
        MaterialCardView outside_box = view.findViewById(R.id.outside_box);
        MaterialCardView inside_box = view.findViewById(R.id.inside_box);
        TextView alert_message = view.findViewById(R.id.alert_message);
        TextView event_name = view.findViewById(R.id.event_name);
        ImageView notifType = view.findViewById(R.id.notif_type_icon);

        assert notif != null;
        // Changes text depending on if the user is being notified that there were selected or not
        if (!notif.selected){
            outside_box.setCardBackgroundColor(Color.parseColor("#F9EEFD"));
            alert_message.setText("You have not been selected to enroll in:");
            notifType.setImageResource(R.drawable.not_accepted_icon);
        }
        else {
            alert_message.setText("You have been selected to enroll in:");
            notifType.setImageResource(R.drawable.accepted_icon);
        }
        // Added cancelled if statement in case the user is cancelled instead of selected
        if (notif.cancelled){
            outside_box.setCardBackgroundColor(Color.parseColor("#F9EEFD"));
            alert_message.setText("Cancelled from event:");
            notifType.setImageResource(R.drawable.cancelled_icon);
        }


        // set event_name to the event name where the notification is coming from
        event_name.setText(notif.getEvent_name());

        return view;

    }


}
