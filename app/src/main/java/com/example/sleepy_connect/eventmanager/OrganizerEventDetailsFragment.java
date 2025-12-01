package com.example.sleepy_connect.eventmanager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.sleepy_connect.CommunityCentre;
import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.EntrantDAL;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.EventViewModel;
import com.example.sleepy_connect.Image;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.SignUpFragment;
import com.example.sleepy_connect.UserViewModel;
import com.example.sleepy_connect.entrantmanagement.EntrantManagerFragment;
import com.example.sleepy_connect.eventdetails.EventDetailsFragment;
import com.example.sleepy_connect.eventdetails.LotteryGuidelinesFragment;
import com.example.sleepy_connect.eventdetails.QRCodeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class OrganizerEventDetailsFragment extends Fragment {
    Event event;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM. d, yyyy", Locale.getDefault());

    public OrganizerEventDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method for this fragment
     * @return A new instance of fragment OrganizerEventDetailsFragment.
     */
    public static OrganizerEventDetailsFragment newInstance(Event event) {
        OrganizerEventDetailsFragment fragment = new OrganizerEventDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.organizer_event_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        event = (Event) args.getSerializable("event");

        // initialize QR Code generator, set visibility of QR code views
        LinearLayout generateQRCodeButton = view.findViewById(R.id.qr_box);
        generateQRCodeButton.setOnClickListener(v -> openQRCodeFragment());

        // set the fields from received event model
        setFields(view);
    }

    /**
     * Set each view's content depending on data received
     * @param view Fragment's root view
     */
    @SuppressLint("DefaultLocale")
    public void setFields(View view) {

        // set title
        TextView title = view.findViewById(R.id.event_title_display);
        title.setText(event.getEventName());

        // set location
        TextView location = view.findViewById(R.id.event_location);
        location.setText(formatLocation());

        // set registration date
        TextView regPeriod = view.findViewById(R.id.reg_deadline);
        String endStr = dateFormat.format(new Date(event.getRegistrationCloses()));
        regPeriod.setText(endStr);

        // set event date
        TextView eventPeriod = view.findViewById(R.id.details_event_dates);
        eventPeriod.setText(formatDatePeriod(event.getEventStartDate(), event.getEventEndDate()));

        // set time
        TextView time = view.findViewById(R.id.event_time);
        time.setText(event.getEventTime());

        // set day of week
        TextView dayOfWeek = view.findViewById(R.id.dayOfWeek_label);
        dayOfWeek.setText(event.getEventDayOfWeek());

        // set description
        if (event.getDescription() != null) {
            TextView description = view.findViewById(R.id.event_description);
            description.setText(event.getDescription());
        }

        // set current waitlist size display
        TextView waitlistSize = view.findViewById(R.id.waitlist_count_display);
        waitlistSize.setText(String.valueOf(event.getWaitlistSize()));

        // set poster if provided
        if (event.getPoster() != null) {
            ImageView poster = view.findViewById(R.id.event_details_poster);
            poster.setBackground(null);
            Image img = new Image(event.getPoster());
            poster.setImageBitmap(img.decodeImage());
        }
    }

    /**
     * Formats community centre info into one string to be displayed
     * @return Formatted community centre info string
     */
    public String formatLocation() {
        CommunityCentre recCenter = event.getCommunityCentre();
        return recCenter.getCommunityCentreName() + "\n" + recCenter.getCommunityCentreLocation();
    }

    /**
     * Formats a given date period string from the given start and end dates
     * @param start Start date
     * @param end End date
     * @return Formatted date period string
     */
    public String formatDatePeriod(long start, long end) {

        // format reg start and end dates
        String startStr = dateFormat.format(new Date(start));
        String endStr = dateFormat.format(new Date(end));
        return startStr + " - " + endStr;
    }

    protected void openQRCodeFragment() {
        //TextView errorText = requireView().findViewById(R.id.qr_code_error_text);

        String eventID = event.getEventID();
        // checks event id is not null(default string value)
        if (eventID != null) {
            // generates QR code, opens up fragment with code
            //errorText.setVisibility(View.GONE);
            QRCodeFragment qrCodeFragment = QRCodeFragment.newInstance(eventID);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, qrCodeFragment)
                    .addToBackStack(null)
                    .commit();
        }

        else {
            //errorText.setVisibility(View.VISIBLE);
            return;
        }
    }
}
