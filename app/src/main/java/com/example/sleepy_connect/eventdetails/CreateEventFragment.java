package com.example.sleepy_connect.eventdetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sleepy_connect.R;

/**
 * Fragment class for creating new events
 */
public class CreateEventFragment extends Fragment {

    private static final String UID = "uid"; // to be updated
    private String uid;
    private boolean imageSet = false;

    EditText title;
    EditText reg_start;
    EditText reg_end;
    EditText event_start;
    EditText capacity;
    EditText description;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uid Current user's id.
     * @return A new instance of fragment CreateEventFragment.
     */
    public static CreateEventFragment newInstance(String uid) {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle args = new Bundle();
        args.putString(UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve parameters
        if (getArguments() != null) {
            uid = getArguments().getString(UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // link views

        ImageView poster = view.findViewById(R.id.edit_event_poster);
        SwitchCompat geolocation = view.findViewById(R.id.edit_geolocation_switch);
        Button save = view.findViewById(R.id.event_confirm_edit_button);

        title = view.findViewById(R.id.edit_event_title);
        reg_start = view.findViewById(R.id.edit_reg_start_date);
        reg_end = view.findViewById(R.id.edit_reg_end_date);
        event_start = view.findViewById(R.id.edit_event_start_date);
        capacity = view.findViewById(R.id.edit_capacity_value);
        description = view.findViewById(R.id.edit_event_descr_text);

        // set listeners
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        geolocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}