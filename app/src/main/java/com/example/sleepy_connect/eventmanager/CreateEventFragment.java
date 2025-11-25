package com.example.sleepy_connect.eventmanager;

import static androidx.core.content.res.ResourcesCompat.getDrawable;

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
import androidx.lifecycle.ViewModelProvider;

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
import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.EntrantDAL;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.Image;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.UserViewModel;
import com.example.sleepy_connect.eventdetails.QRCodeFragment;

import java.io.IOException;
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
    private Event event;
    private final SimpleDateFormat format = new SimpleDateFormat("EEE MMM d, y", Locale.getDefault());

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

    public CreateEventFragment() {}

    public static CreateEventFragment newInstance() {
        return new CreateEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //change toolbar title
        TextView title = requireActivity().findViewById(R.id.set_title);
        title.setText("Create Event");

        // set poster imageview's listener
        ivPoster = view.findViewById(R.id.edit_event_poster);
        ivPoster.setOnClickListener(v -> pickMedia.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()
        ));

        // Date pickers
        view.findViewById(R.id.edit_reg_start_button).setOnClickListener(v -> updateDateTime(view, R.id.edit_reg_start_date));
        view.findViewById(R.id.edit_reg_end_button).setOnClickListener(v -> updateDateTime(view, R.id.edit_reg_end_date));
        view.findViewById(R.id.edit_event_start_button).setOnClickListener(v -> updateDateTime(view, R.id.edit_event_start_date));
        view.findViewById(R.id.edit_event_end_button).setOnClickListener(v -> updateDateTime(view, R.id.edit_event_end_date));

        // Geolocation switch
        SwitchCompat geolocationSwitch = view.findViewById(R.id.edit_geolocation_switch);
        geolocationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> geolocationOn = isChecked);

        // Save event button
        TextView saveBtn = view.findViewById(R.id.event_confirm_edit_button);
        saveBtn.setOnClickListener(v -> saveEvent(view));
    }

    /**
     * Saves user input in the database when required fields are complete, otherwise nothing is saved
     * @param view The fragment's root view
     */
    private void saveEvent(View view) {

        EditText etTitle = view.findViewById(R.id.edit_event_title);
        EditText etEventCapacity = view.findViewById(R.id.edit_event_capacity_value);
        EditText etRecCenter = view.findViewById(R.id.edit_host_rec_center_text);
        EditText etAddress = view.findViewById(R.id.edit_host_address_text);
        EditText etEventStartTime = view.findViewById(R.id.edit_event_time_start_time);
        EditText etEventEndTime = view.findViewById(R.id.edit_event_time_end_time);
        TextView tvRegStartDate = view.findViewById(R.id.edit_reg_start_date);
        TextView tvRegEndDate = view.findViewById(R.id.edit_reg_end_date);
        TextView tvEventStartDate = view.findViewById(R.id.edit_event_start_date);
        TextView tvEventEndDate = view.findViewById(R.id.edit_event_end_date);

        // check if mandatory fields are filled
        if (!mandatoryFieldsFilled(etTitle, etEventCapacity, etRecCenter, etAddress, tvRegStartDate, tvRegEndDate, tvEventStartDate)) {
            return;
        }

        // get user from viewmodel
        UserViewModel vmUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        user = vmUser.getUser().getValue();
        assert user != null;

        // save data in the database
        EventDAL eventDal = new EventDAL();
        eventDal.getNextID(newID -> {
            Log.d("EventDAL", "New ID: " + newID);

            CommunityCentreDAL communityDal = new CommunityCentreDAL();
            String centreName = etRecCenter.getText().toString();
            String centreAddress = etAddress.getText().toString();

            // Gets all community centres TODO: Matching doesnt actually work
            communityDal.getCommunityCentres(centres -> {
                CommunityCentre match = null;

                // Matches all community centres
                for (CommunityCentre c : centres) {
                    if (c.getCommunityCentreName().equalsIgnoreCase(centreName)
                            || c.getCommunityCentreLocation().equalsIgnoreCase(centreAddress)) {
                        match = c;
                        break;
                    }
                }

                // Found a match
                CommunityCentre recCenter = (match != null)
                        ? match
                        : new CommunityCentre(centreName, centreAddress);

                // Didnt find a match
                if (match == null) {
                    communityDal.addCommunityCentre(recCenter);
                    Log.d("CreateEventFragment", "New community centre: " + centreName);
                } else {
                    Log.d("CreateEventFragment", "Old community centre: " + match.getCommunityCentreName());
                }

                try {
                    event = new Event(
                            newID,
                            etTitle.getText().toString(),
                            recCenter,
                            user.getAndroid_id(),
                            Objects.requireNonNull(format.parse(tvRegStartDate.getText().toString())).getTime(),
                            Objects.requireNonNull(format.parse(tvRegEndDate.getText().toString())).getTime(),
                            Objects.requireNonNull(format.parse(tvEventStartDate.getText().toString())).getTime(),
                            Objects.requireNonNull(format.parse(tvEventEndDate.getText().toString())).getTime(),
                            createTimeString(etEventStartTime, etEventEndTime),
                            Integer.parseInt(etEventCapacity.getText().toString()),
                            geolocationOn
                    );

                    TextView description = view.findViewById(R.id.edit_event_descr_text);
                    if (!description.getText().toString().isEmpty()) {
                        event.setDescription(description.getText().toString());
                    }

                    EditText etWaitlistCap = view.findViewById(R.id.edit_waitlist_capacity_value);
                    String waitlistCapStr = etWaitlistCap.getText().toString();
                    if (!waitlistCapStr.isEmpty()) {
                        event.setWaitlistCapacity(Integer.parseInt(waitlistCapStr));
                    }

                    if (posterUri != null) {
                        Image img = new Image(getContext(), posterUri);
                        event.setPoster(img.getBase64String());
                    }

                    // Add event to this rec centre
                    recCenter.addEvent(event.getEventID());
                    communityDal.updateCommunityCentre(recCenter);

                    // Add to All Locations
                    communityDal.getCommunityCentre("All Locations", allEventsCentre -> {
                        if (allEventsCentre != null) {
                            allEventsCentre.addEvent(event.getEventID());
                            communityDal.updateCommunityCentre(allEventsCentre);
                        } else {
                            CommunityCentre allEvents = new CommunityCentre("All Locations", "See all events");
                            allEvents.addEvent(event.getEventID());
                            communityDal.addCommunityCentre(allEvents);
                        }
                    });

                    // Add event to entrant and DB
                    user.addCreatedEvent(event.getEventID());
                    new EntrantDAL().updateEntrant(user);
                    eventDal.addEvent(event);

                    if (isAdded()) requireActivity().getSupportFragmentManager().popBackStack();

                    // generate QR code, go to QRCode fragment automatically.
                    openQRCodeFragment(newID);

                } catch (ParseException | IOException e) {
                    Log.e("CreateEventFragment", "Error creating event", e);
                }
            });
        });
    }

    /**
     * Combines input from the event start and end time fields, separated by a "-"
     * @param etEventStart edittext containing event start time input
     * @param etEventEnd edittext containing event end time input
     * @return String combining both time inputs
     */
    private String createTimeString(EditText etEventStart, EditText etEventEnd) {
        return etEventStart.getText().toString() + "-" + etEventEnd.getText().toString();
    }

    /**
     * Call DatePickerDialog to update the given view's (given by id) text field
     * @param rootView Fragment's root view
     * @param dateViewId Date view's id to be updated
     */
    private void updateDateTime(View rootView, int dateViewId) {
        Calendar today = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {

                    // create new date
                    Calendar date = Calendar.getInstance();
                    date.set(year, month, dayOfMonth);

                    // set text in view
                    String dateString = format.format(date.getTime());
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

    protected void openQRCodeFragment(String eventID) {
        TextView errorText = requireView().findViewById(R.id.qr_code_error_text);

        eventID = "sleepyEventApp/".concat(eventID);
        // checks event id is not null(default string value)
        if (eventID != null) {
            // generates QR code, opens up fragment with code
            errorText.setVisibility(View.GONE);
            QRCodeFragment qrCodeFragment = QRCodeFragment.newInstance(eventID);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, qrCodeFragment)
                    .addToBackStack(null)
                    .commit();
        }

        else {
            errorText.setVisibility(View.VISIBLE);
        }
    }
}
