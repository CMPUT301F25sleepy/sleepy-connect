package com.example.sleepy_connect.eventdetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEventFragment extends Fragment implements EventDAL.OnEventRetrievedListener {

    // arguments for the edit event fragment
    private static final String ARG_EVENT_NAME = "eventName";
    private static final String ARG_EVENT_ID = "eventID";

    // TODO: Rename and change types of parameters
    // pass in event name and ID to this fragment
    private String eventName;
    private String eventID;

    private EventDAL eventDAL = new EventDAL();

    public EditEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eventName Name of selected event from My Events list.
     * @param eventID Unique event ID of selected event.
     * @return A new instance of fragment EditEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditEventFragment newInstance(String eventName, String eventID) {
        EditEventFragment fragment = new EditEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_NAME, eventName);
        args.putString(ARG_EVENT_ID, eventID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventName = getArguments().getString(ARG_EVENT_NAME);
            eventID = getArguments().getString(ARG_EVENT_ID);
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

        ConstraintLayout qrCodeLayout = view.findViewById(R.id.generate_qr_code_layout);
        qrCodeLayout.setVisibility(View.VISIBLE);

        Button generateQRCodeButton = view.findViewById(R.id.generate_qr_code_button);
        generateQRCodeButton.setOnClickListener(v -> openQRCodeFragment(v, R.id.generate_qr_code_button));

    }

    public Event EventRetrievedListener implements EventDAL.OnEventRetrievedListener() {

    }

    // open fragment showing generated QR code
    public void openQRCodeFragment(View v, int generateQRCodeButtonID, Event event) {
        // make sure there's an event title
        TextView eventTitle = requireView().findViewById(R.id.edit_event_title);

        // ensure the event name and ID match what's in the database
        Event selectedEvent = eventDAL.getEvent(eventID, null);
        //if(isComplete(eventTitle, true)) {

        // grab event title and event ID from DAL

            // event title passed to QR code fragment
            QRCodeFragment qrCodeFragment = QRCodeFragment.newInstance(eventTitle.getText().toString());

            // create and open fragment, passing in event title as string to encode
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, qrCodeFragment)
                    .addToBackStack(null)
                    .commit();
        //}

        //else {
            TextView errorText = requireView().findViewById(R.id.qr_code_error_text);
            errorText.setVisibility(View.VISIBLE);
        //}

        // TODO: check if QR code exists in database
        // TODO: store in database if not already in
        // TODO: allow copying of QR code image
    }

    public void onEventRetrieved(Event event) {
        this.eventName = event.getEventID();
        even
    }
}