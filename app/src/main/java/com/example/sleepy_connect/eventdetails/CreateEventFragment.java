package com.example.sleepy_connect.eventdetails;

import static androidx.core.content.res.ResourcesCompat.getDrawable;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sleepy_connect.R;

import java.net.URI;

/**
 * Fragment class for creating new events
 * @author Sam Francisco
 */
public class CreateEventFragment extends Fragment {

    private static final String UID = "uid"; // to be updated
    private String uid;
    private boolean geolocationOn = false;
    private Uri posterUri = null;

    // Registers a photo picker activity launcher in single-select mode.
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    posterUri = uri;
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

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

        // initiate view listeners

        ConstraintLayout regStart = view.findViewById(R.id.edit_reg_start_button);
        regStart.setOnClickListener(v -> updateDateTime(v, R.id.edit_reg_start_date, R.id.edit_reg_start_time));

        ConstraintLayout regEnd = view.findViewById(R.id.edit_reg_end_button);
        regEnd.setOnClickListener(v -> updateDateTime(v, R.id.edit_reg_end_date, R.id.edit_reg_end_time));

        ConstraintLayout eventStart = view.findViewById(R.id.edit_event_start_button);
        eventStart.setOnClickListener(v -> updateDateTime(v, R.id.edit_event_start_date, R.id.edit_event_start_time));

        ConstraintLayout eventEnd = view.findViewById(R.id.edit_event_end_button);
        eventEnd.setOnClickListener(v -> updateDateTime(v, R.id.edit_event_end_date, R.id.edit_event_end_time));

        ImageView poster = view.findViewById(R.id.edit_event_poster);
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

            }
        });

        SwitchCompat geolocationSwitch = view.findViewById(R.id.edit_geolocation_switch);
        geolocationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // record state when changed
            geolocationOn = isChecked;
        });

        Button saveBtn = view.findViewById(R.id.event_confirm_edit_button);
        saveBtn.setOnClickListener(v -> {

            // link views
            EditText title = view.findViewById(R.id.edit_event_title);
            EditText eventCapacity = view.findViewById(R.id.edit_event_capacity_value);
            EditText waitlistCapacity = view.findViewById(R.id.edit_waitlist_capacity_value);
            EditText recCenter = view.findViewById(R.id.edit_host_rec_center_text);
            EditText address = view.findViewById(R.id.edit_host_address_text);
            EditText description = view.findViewById(R.id.edit_event_descr_text);
            TextView regStartDate = view.findViewById(R.id.edit_reg_start_date);
            TextView regStartTime = view.findViewById(R.id.edit_reg_start_time);
            TextView regEndDate = view.findViewById(R.id.edit_reg_end_date);
            TextView regEndTime = view.findViewById(R.id.edit_reg_end_time);
            TextView eventStartDate = view.findViewById(R.id.edit_event_start_date);
            TextView eventStartTime = view.findViewById(R.id.edit_event_start_time);
            TextView eventEndDate = view.findViewById(R.id.edit_event_end_date);
            TextView eventEndTime = view.findViewById(R.id.edit_event_end_time);

            // check if mandatory fields are set and set their backgrounds if not
            if (!mandatoryFieldsFilled(title, eventCapacity, recCenter, address, regStartDate, regEndDate, eventStartDate)) {
                return;
            }

            // TODO: store data in database

        });
    }

    /**
     *
     * @param view
     * @param dateViewId
     * @param timeViewId
     */
    public void updateDateTime(View view, int dateViewId, int timeViewId) {

        // TODO: open date time picker activity and get date and time

        // TODO: update event_end date and time
        TextView dateView = view.findViewById(dateViewId);
        TextView timeView = view.findViewById(timeViewId);

    }

    /**
     *
     * @param title
     * @param eventCapacity
     * @param recCenter
     * @param address
     * @param regStartDate
     * @param regEndDate
     * @param eventStartDate
     * @return
     */
    public boolean mandatoryFieldsFilled(EditText title, EditText eventCapacity, EditText recCenter, EditText address,
                                     TextView regStartDate, TextView regEndDate, TextView eventStartDate) {

        // bitwise & all the views' values' isComplete
        boolean complete = isComplete(title) & isComplete(eventCapacity) & isComplete(recCenter) &
                isComplete(address) & isComplete(regStartDate) & isComplete(eventStartDate);

        // show error message if not complete
        if (!complete) {
            assert getView() != null;
            TextView errorBox = getView().findViewById(R.id.edit_event_error_box);
            errorBox.setVisibility(View.VISIBLE);
        }

        return complete;
    }

    /**
     *
     * @param tv
     * @return
     */
    public boolean isComplete(TextView tv) {

        if (tv.getText().length() == 0) {
            // highlight red if empty
            tv.setBackground(getDrawable(getResources(), R.drawable.error_text_border, null));
            return false;
        }
        return true;
    }
}