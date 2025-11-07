package com.example.sleepy_connect.eventdetails;

import static androidx.core.content.res.ResourcesCompat.getDrawable;

import android.app.DatePickerDialog;
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
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sleepy_connect.CommunityCentre;
import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.EntrantDAL;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.Image;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.UserViewModel;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

/**
 * Fragment class for creating new events
 * @author Sam Francisco
 */
public class CreateEventFragment extends Fragment {
    private Entrant user;
    private boolean geolocationOn = false;
    private Uri posterUri = null;
    private ImageView ivPoster;
    SimpleDateFormat format = new SimpleDateFormat("EEE MMM d, y", Locale.getDefault());

    // Registers a photo picker activity launcher in single-select mode.
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);

                    // record uri, load new image on imageview
                    posterUri = uri;
                    ivPoster.setImageURI(uri);

                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    // Required empty public constructor
    public CreateEventFragment() {}

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CreateEventFragment.
     */
    public static CreateEventFragment newInstance() {
        return new CreateEventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get current user from viewmodel
        user = UserViewModel.getUser().getValue();
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
        regStart.setOnClickListener(v -> updateDateTime(v, R.id.edit_reg_start_date));

        ConstraintLayout regEnd = view.findViewById(R.id.edit_reg_end_button);
        regEnd.setOnClickListener(v -> updateDateTime(v, R.id.edit_reg_end_date));

        ConstraintLayout eventStart = view.findViewById(R.id.edit_event_start_button);
        eventStart.setOnClickListener(v -> updateDateTime(v, R.id.edit_event_start_date));

        ConstraintLayout eventEnd = view.findViewById(R.id.edit_event_end_button);
        eventEnd.setOnClickListener(v -> updateDateTime(v, R.id.edit_event_end_date));

        Button generateQRCodeButton = view.findViewById(R.id.generate_qr_code_button);
        generateQRCodeButton.setOnClickListener(v -> openQRCodeFragment(v, R.id.generate_qr_code_button));


        ivPoster = view.findViewById(R.id.edit_event_poster);
        ivPoster.setOnClickListener(new View.OnClickListener() {
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

        TextView saveBtn = view.findViewById(R.id.event_confirm_edit_button);
        saveBtn.setOnClickListener(v -> {

            // set mandatory values
            Event newEvent = setMandatoryEventData(view);
            if (newEvent == null) {
                return;
            }

            // set optional values
            try {
                setOptionalEventData(view, newEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // store event in db
            EventDAL eventDal = new EventDAL();
            eventDal.addEvent(newEvent);

            // update entrant
            EntrantDAL entrantDal = new EntrantDAL();
            user.addCreatedEvent(newEvent.getEventID());
            entrantDal.updateEntrant(user);
        });
    }

    public Event setMandatoryEventData(View root) {

        // link views
        EditText etTitle = root.findViewById(R.id.edit_event_title);
        EditText etEventCapacity = root.findViewById(R.id.edit_event_capacity_value);
        EditText etRecCenter = root.findViewById(R.id.edit_host_rec_center_text);
        EditText etAddress = root.findViewById(R.id.edit_host_address_text);
        EditText etEventStartTime = root.findViewById(R.id.edit_event_time_start_time);
        EditText etEventEndTime = root.findViewById(R.id.edit_event_time_end_time);
        TextView tvRegStartDate = root.findViewById(R.id.edit_reg_start_date);
        TextView tvRegEndDate = root.findViewById(R.id.edit_reg_end_date);
        TextView tvEventStartDate = root.findViewById(R.id.edit_event_start_date);
        TextView tvEventEndDate = root.findViewById(R.id.edit_event_end_date);


        // check if mandatory fields are set and set their backgrounds if not
        if (!mandatoryFieldsFilled(etTitle, etEventCapacity, etRecCenter, etAddress, tvRegStartDate, tvRegEndDate, tvEventStartDate)) {
            return null;
        }

        // create event object with mandatory attributes
        Event event = null;
        try {
            event = new Event(
                    etTitle.getText().toString(),
                    new CommunityCentre(etRecCenter.getText().toString(), etAddress.getText().toString()),
                    user.getAndroid_id(),
                    Objects.requireNonNull(format.parse((String) tvRegStartDate.getText())).getTime(),
                    Objects.requireNonNull(format.parse((String) tvRegEndDate.getText())).getTime(),
                    Objects.requireNonNull(format.parse((String) tvEventStartDate.getText())).getTime(),
                    Objects.requireNonNull(format.parse((String) tvEventEndDate.getText())).getTime(),
                    createTimeString(etEventStartTime, etEventEndTime),
                    Integer.parseInt(etEventCapacity.getText().toString()),
                    geolocationOn
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return event;
    }

    public void setOptionalEventData(View root, Event newEvent) throws IOException {

        // set description
        TextView description = root.findViewById(R.id.edit_event_descr_text);
        newEvent.setDescription(description.getText().toString());

        // set poster if provided by user
        if (posterUri != null) {
            newEvent.setPoster(new Image(getContext(), posterUri));
        }

        // set waitlist capacity if provided by user
        EditText etWaitlistCap = root.findViewById(R.id.edit_waitlist_capacity_value);
        String waitlistCapStr = etWaitlistCap.getText().toString();
        if (!waitlistCapStr.isEmpty()) {
            newEvent.setWaitlistCapacity(Integer.parseInt(waitlistCapStr));
        }
    }

    public String createTimeString(EditText etEventStart, EditText etEventEnd) {

        // get string values
        String start = etEventStart.getText().toString();
        String end = etEventEnd.getText().toString();

        // return formatted concatenated time string
        return start + "-" + end;
    }

    /**
     *
     * @param rootView
     * @param dateViewId
     */
    public void updateDateTime(View rootView, int dateViewId) {

        // open date picker
        Calendar today = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {

                    // get selected date
                    Calendar date = Calendar.getInstance();
                    date.set(year, month, dayOfMonth);
                    String dateString = format.format(date.getTime());

                    // update textview
                    TextView tv = rootView.findViewById(dateViewId);
                    tv.setText(dateString);
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        );

        // show date picker dialog
        datePickerDialog.show();
    }

    // open fragment showing generated QR code
    public void openQRCodeFragment(View v, int generateQRCodeButtonID) {
        // make sure there's an event title
        TextView eventTitle = requireView().findViewById(R.id.edit_event_title);

        if(isComplete(eventTitle, true)) {
            // event title passed to QR code fragment
            QRCodeFragment qrCodeFragment = QRCodeFragment.newInstance(eventTitle.getText().toString());

            // create and open fragment, passing in event title as string to encode
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, qrCodeFragment)
                    .addToBackStack(null)
                    .commit();
        }

        else {
            TextView errorText = requireView().findViewById(R.id.qr_code_error_text);
            errorText.setVisibility(View.VISIBLE);
        }

        // TODO: check if QR code exists in database
        // TODO: store in database if not already in
        // TODO: allow copying of QR code image
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
        boolean complete = isComplete(title, false) & isComplete(eventCapacity, true) & isComplete(recCenter, true) &
                isComplete(address, true) & isComplete(regStartDate, true) & isComplete(eventStartDate, true);

        // link error box
        assert getView() != null;
        TextView errorBox = getView().findViewById(R.id.edit_event_error_box);

        // show error message if not complete
        if (!complete) {
            errorBox.setVisibility(View.VISIBLE);

        // remove error message if complete
        } else {
            errorBox.setVisibility(View.GONE);
        }

        return complete;
    }

    /**
     *
     * @param tv
     * @return
     */
    public boolean isComplete(TextView tv, boolean hasViewGroup) {

        if (tv.getText().length() == 0) {
            // highlight red if empty
            tv.setBackground(getDrawable(getResources(), R.drawable.error_text_border, null));
            return false;

        } else {
            // remove highlight if view is filled
            if (hasViewGroup) {
                tv.setBackground(null);
            } else {
                tv.setBackground(getDrawable(getResources(), R.drawable.light_text_border, null));
            }
            return true;
        }
    }
}