package com.example.sleepy_connect.eventmanager;

import static androidx.core.content.res.ResourcesCompat.getDrawable;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sleepy_connect.CommunityCentre;
import com.example.sleepy_connect.CommunityCentreDAL;
import com.example.sleepy_connect.EntrantDAL;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.Image;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.UserViewModel;
import com.example.sleepy_connect.eventdetails.QRCodeFragment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Fragment class for editing existing events.
 * @author Sam Francisco
 */
public class EditEventFragment extends Fragment {

    private Event event;
    private Uri posterUri;
    private Boolean geolocationOn;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d, y", Locale.getDefault());

    // views
    private EditText etTitle;
    private TextView tvRegStartDate;
    private TextView tvRegEndDate;
    private TextView tvEventStartDate;
    private TextView tvEventEndDate;
    private EditText etStartTime;
    private EditText etEndTime;
    private EditText etEventCapacity;
    private EditText etWaitlistCapacity;
    private EditText etLocationName;
    private EditText etLocationAddress;
    private SwitchCompat switchGeolocation;
    private EditText etDescription;
    private ImageView ivPoster;

    // Registers a photo picker activity launcher in single-select mode.
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    posterUri = uri;
                    ivPoster.setImageURI(uri);

                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    public EditEventFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method for this fragment.
     * @return A new instance of fragment EditEventFragment.
     */
    public static EditEventFragment newInstance() {
        return new EditEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // change toolbar title
        TextView title = requireActivity().findViewById(R.id.set_title);
        title.setText("Edit Event");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // receive event details from viewmodel
        event = EventViewModel.getEvent().getValue();

        // set the fields from received event model
        initViewReferences(view);
        setFields(view);

        // set poster imageview's listener
        ivPoster = view.findViewById(R.id.edit_event_poster);
        ivPoster.setOnClickListener(v -> pickMedia.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()
        ));

        // set date picker listeners
        view.findViewById(R.id.edit_reg_start_button).setOnClickListener(v -> updateDateTime(view, R.id.edit_reg_start_date));
        view.findViewById(R.id.edit_reg_end_button).setOnClickListener(v -> updateDateTime(view, R.id.edit_reg_end_date));
        view.findViewById(R.id.edit_event_start_button).setOnClickListener(v -> updateDateTime(view, R.id.edit_event_start_date));
        view.findViewById(R.id.edit_event_end_button).setOnClickListener(v -> updateDateTime(view, R.id.edit_event_end_date));

        // Geolocation switch
        SwitchCompat geolocationSwitch = view.findViewById(R.id.edit_geolocation_switch);
        geolocationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> geolocationOn = isChecked);

        // initialize QR Code generator
        Button generateQRCodeButton = view.findViewById(R.id.generate_qr_code_button);
        generateQRCodeButton.setOnClickListener(v -> openQRCodeFragment());

        // Save event button
        TextView saveBtn = view.findViewById(R.id.event_confirm_edit_button);
        saveBtn.setOnClickListener(v -> saveEvent(view));
    }

    /**
     * Link the fragment's views
     * @param view Fragment's root view
     */
    public void initViewReferences(View view) {
        etTitle = view.findViewById(R.id.edit_event_title);
        tvRegStartDate = view.findViewById(R.id.edit_reg_start_date);
        tvRegEndDate = view.findViewById(R.id.edit_reg_end_date);
        tvEventStartDate = view.findViewById(R.id.edit_event_start_date);
        tvEventEndDate = view.findViewById(R.id.edit_event_end_date);
        etStartTime = view.findViewById(R.id.edit_event_time_start_time);
        etEndTime = view.findViewById(R.id.edit_event_time_end_time);
        etEventCapacity = view.findViewById(R.id.edit_event_capacity_value);
        etWaitlistCapacity = view.findViewById(R.id.edit_waitlist_capacity_value);
        etLocationName = view.findViewById(R.id.edit_host_rec_center_text);
        etLocationAddress = view.findViewById(R.id.edit_host_address_text);
        switchGeolocation = view.findViewById(R.id.edit_geolocation_switch);
        etDescription = view.findViewById(R.id.edit_event_descr_text);
        ivPoster = view.findViewById(R.id.edit_event_poster);
    }

    /**
     * Set fields to initial event data
     * @param view Fragment's root view
     */
    @SuppressLint("DefaultLocale")
    public void setFields(View view) {

        etTitle.setText(event.getEventName());
        tvRegStartDate.setText(dateFormat.format(new Date(event.getRegistrationOpens())));
        tvRegEndDate.setText(dateFormat.format(new Date(event.getRegistrationCloses())));
        tvEventStartDate.setText(dateFormat.format(new Date(event.getEventStartDate())));
        tvEventEndDate.setText(dateFormat.format(new Date(event.getEventEndDate())));
        etEventCapacity.setText(String.format("%d", event.getEventCapacity()));
        etLocationName.setText(event.getCommunityCentre().getCommunityCentreName());
        etLocationAddress.setText(event.getCommunityCentre().getCommunityCentreLocation());

        // get time
        String[] times = event.getEventTime().split("-");
        etStartTime.setText(times[0].strip());
        etEndTime.setText(times[1].strip());

        // get geolocation
        switchGeolocation.setChecked(event.isGeolocationEnabled());
        geolocationOn = event.isGeolocationEnabled();

        // get waitlist capacity
        if (event.getWaitlistCapacity() != Integer.MAX_VALUE) {
            etWaitlistCapacity.setText(String.format("%d", event.getWaitlistCapacity()));
        }

        // set description
        if (event.getDescription() != null) {
            etDescription.setText(event.getDescription());
        }

        // set poster if provided
        if (event.getPoster() != null) {
            ivPoster.setImageBitmap(event.getPoster().decodeImage());
        }
    }

    // TODO: update encoded info in QR
    protected void openQRCodeFragment() {
        TextView eventTitle = requireView().findViewById(R.id.edit_event_title);
        TextView errorText = requireView().findViewById(R.id.qr_code_error_text);

        if (!eventTitle.getText().toString().isEmpty()) {
            errorText.setVisibility(View.GONE);
            QRCodeFragment qrCodeFragment = QRCodeFragment.newInstance(eventTitle.getText().toString());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, qrCodeFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            errorText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Call DatePickerDialog to update the given view's (given by id) text field
     * @param rootView Fragment's root view
     * @param dateViewId Date view's id to be updated
     */
    public void updateDateTime(View rootView, int dateViewId) {
        Calendar today = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {

                    // create new date
                    Calendar date = Calendar.getInstance();
                    date.set(year, month, dayOfMonth);

                    // set text in view
                    String dateString = dateFormat.format(date.getTime());
                    TextView tv = rootView.findViewById(dateViewId);
                    tv.setText(dateString);
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /**
     * Checks passed view fields if complete
     * @param title Title view
     * @param eventCapacity Event capacity view
     * @param recCenter Community centre name view
     * @param address Community centre address view
     * @param regStartDate Registration start date view
     * @param regEndDate Registration end date view
     * @param eventStartDate Registration start date view
     * @return True if passed views are filled, false otherwise
     */
    private boolean mandatoryFieldsFilled(EditText title, EditText eventCapacity, EditText recCenter, EditText address,
                                          TextView regStartDate, TextView regEndDate, TextView eventStartDate) {

        // check each view
        boolean complete = isComplete(title, false)
                & isComplete(eventCapacity, true)
                & isComplete(recCenter, true)
                & isComplete(address, true)
                & isComplete(regStartDate, true)
                & isComplete(eventStartDate, true)
                & isComplete(regEndDate, true);

        // set error message visibility to gone if complete, show otherwise
        TextView errorBox = requireView().findViewById(R.id.edit_event_error_box);
        errorBox.setVisibility(complete ? View.GONE : View.VISIBLE);

        return complete;
    }

    /**
     * Check passed view if filled
     * @param tv Textview to check
     * @param hasViewGroup Indicates if drawable border needs to be cleared or reset
     * @return True if given view is filled, false otherwise
     */
    private boolean isComplete(TextView tv, boolean hasViewGroup) {

        // empty view: highlight border
        if (tv.getText().length() == 0) {
            tv.setBackground(getDrawable(getResources(), R.drawable.error_text_border, null));
            return false;

            // nonempty view: restore border to light_text_border if view not within a viewgroup, clear border otherwise
        } else {
            tv.setBackground(hasViewGroup ? null :
                    getDrawable(getResources(), R.drawable.light_text_border, null));
            return true;
        }
    }

    /**
     * Saves user input in the database when required fields are complete, otherwise nothing is saved
     * @param view The fragment's root view
     */
    private void saveEvent(View view) {

        // check if mandatory fields are filled
        if (!mandatoryFieldsFilled(etTitle, etEventCapacity, etLocationName, etLocationAddress, tvRegStartDate, tvRegEndDate, tvEventStartDate)) {
            return;
        }

        // TODO: update mandatory event model fields

        // update waitlist capacity
        String waitlistCapStr = etWaitlistCapacity.getText().toString();
        if (!waitlistCapStr.isEmpty()) {
            event.setWaitlistCapacity(Integer.parseInt(waitlistCapStr));
        }

        // update poster if changed
        if (posterUri != null) {
            try {
                event.setPoster(new Image(getContext(), posterUri));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // update description
        TextView description = view.findViewById(R.id.edit_event_descr_text);
        if (!description.getText().toString().isEmpty()) {
            event.setDescription(description.getText().toString());
        }

        // update database
        EventDAL dal = new EventDAL();
        dal.updateEvent(event);

    }
}