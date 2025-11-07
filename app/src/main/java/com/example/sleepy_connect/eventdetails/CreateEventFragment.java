package com.example.sleepy_connect.eventdetails;

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
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.EntrantDAL;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.Image;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.UserViewModel;

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

        // QR Code generator
        Button generateQRCodeButton = view.findViewById(R.id.generate_qr_code_button);
        generateQRCodeButton.setOnClickListener(v -> openQRCodeFragment());

        // Geolocation switch
        SwitchCompat geolocationSwitch = view.findViewById(R.id.edit_geolocation_switch);
        geolocationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> geolocationOn = isChecked);

        // Save event button
        TextView saveBtn = view.findViewById(R.id.event_confirm_edit_button);
        saveBtn.setOnClickListener(v -> saveEvent(view));
    }

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

        if (!mandatoryFieldsFilled(etTitle, etEventCapacity, etRecCenter, etAddress, tvRegStartDate, tvRegEndDate, tvEventStartDate)) {
            return;
        }

        user = UserViewModel.getUser().getValue();
        if (user == null) {
            Log.e("CreateEventFragment", "User is null");
            return;
        }

        EventDAL eventDal = new EventDAL();
        eventDal.getNextID(newID -> {
            Log.d("EventDAL", "New ID: " + newID);

            CommunityCentreDAL communityDal = new CommunityCentreDAL();
            String centreName = etRecCenter.getText().toString();
            String centreAddress = etAddress.getText().toString();

            communityDal.getCommunityCentre(centreName, existingCentre -> {
                CommunityCentre recCenter = (existingCentre != null)
                        ? existingCentre
                        : new CommunityCentre(centreName, centreAddress);

                if (existingCentre == null)
                    communityDal.addCommunityCentre(recCenter);

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
                        event.setPoster(new Image(getContext(), posterUri));
                    }

                    // Add event to the rec centre
                    recCenter.addEvent(event.getEventID());
                    communityDal.updateCommunityCentre(recCenter);

                    // Add event to list of all events
                    communityDal.getCommunityCentre("All Locations", allEventsCentre -> {
                        if (allEventsCentre != null) {
                            allEventsCentre.addEvent(event.getEventID());
                            communityDal.updateCommunityCentre(allEventsCentre);
                            Log.d("CreateEventFragment", "Added event to All Events centre");
                        } else {
                            CommunityCentre allEvents = new CommunityCentre("All Locations", "See all events");
                            allEvents.addEvent(event.getEventID());
                            communityDal.addCommunityCentre(allEvents);
                            Log.d("CreateEventFragment", "Created All Locations");
                        }
                    });

                    // Add event to entrant
                    user.addCreatedEvent(event.getEventID());
                    new EntrantDAL().updateEntrant(user);

                    // add event to firebase
                    eventDal.addEvent(event);
                    if (isAdded()) requireActivity().getSupportFragmentManager().popBackStack();

                } catch (ParseException | IOException e) {
                    Log.e("CreateEventFragment", "Error creating event", e);
                }
            });
        });
    }

    private String createTimeString(EditText etEventStart, EditText etEventEnd) {
        return etEventStart.getText().toString() + "-" + etEventEnd.getText().toString();
    }

    private void updateDateTime(View rootView, int dateViewId) {
        Calendar today = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    Calendar date = Calendar.getInstance();
                    date.set(year, month, dayOfMonth);
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

    private void openQRCodeFragment() {
        TextView eventTitle = requireView().findViewById(R.id.edit_event_title);
        TextView errorText = requireView().findViewById(R.id.qr_code_error_text);

        if (isComplete(eventTitle, true)) {
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

    private boolean mandatoryFieldsFilled(EditText title, EditText eventCapacity, EditText recCenter, EditText address,
                                          TextView regStartDate, TextView regEndDate, TextView eventStartDate) {

        boolean complete = isComplete(title, false)
                & isComplete(eventCapacity, true)
                & isComplete(recCenter, true)
                & isComplete(address, true)
                & isComplete(regStartDate, true)
                & isComplete(eventStartDate, true)
                & isComplete(regEndDate, true);

        TextView errorBox = requireView().findViewById(R.id.edit_event_error_box);
        errorBox.setVisibility(complete ? View.GONE : View.VISIBLE);

        return complete;
    }

    private boolean isComplete(TextView tv, boolean hasViewGroup) {
        if (tv.getText().length() == 0) {
            tv.setBackground(getDrawable(getResources(), R.drawable.error_text_border, null));
            return false;
        } else {
            tv.setBackground(hasViewGroup ? null :
                    getDrawable(getResources(), R.drawable.light_text_border, null));
            return true;
        }
    }
}
