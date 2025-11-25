package com.example.sleepy_connect.admin.notificationmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sleepy_connect.Notification;
import com.example.sleepy_connect.R;

import java.util.ArrayList;

public class AdminNotificationListAdapter extends ArrayAdapter<Notification> {

    public AdminNotificationListAdapter(Context context, ArrayList<Notification> notifications) {
        super(context, 0, notifications);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // inflate view
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_admin_notification, parent, false);
        }

        // get notif data
        Notification notif = getItem(position);
        assert notif != null;

        // set notif message
        TextView tvMessage = view.findViewById(R.id.admin_notif_list_message);
        String msg;
        if (!notif.selected) {
            msg = "You have not been selected to enroll in: ";
        } else {
            msg = "You have been selected to enroll in: ";
        }
        if (notif.cancelled) {
            msg = "Cancelled from event: ";
        }
        msg = msg + notif.getEvent_name();
        tvMessage.setText(msg);

        return view;
    }
}
