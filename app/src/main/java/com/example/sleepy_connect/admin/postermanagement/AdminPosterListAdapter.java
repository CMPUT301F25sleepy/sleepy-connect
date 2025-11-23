package com.example.sleepy_connect.admin.postermanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sleepy_connect.Image;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.R;

import java.util.ArrayList;

public class AdminPosterListAdapter extends ArrayAdapter<Event> {

    public AdminPosterListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // inflate view
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_admin_poster, parent, false);
        }

        // get formatted item data
        Event event = getItem(position);
        assert event != null;

        // set imageview image
        ImageView ivPoster = view.findViewById(R.id.admin_poster_list_iv_poster);
        Image img = new Image(event.getPoster());
        ivPoster.setImageBitmap(img.decodeImage());

        return view;
    }
}
