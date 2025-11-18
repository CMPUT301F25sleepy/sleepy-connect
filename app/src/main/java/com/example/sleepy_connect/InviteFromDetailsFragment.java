package com.example.sleepy_connect;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * fragment class to accept to decline an invite through the event details
 */
public class InviteFromDetailsFragment extends DialogFragment {
    public String eventID;
    public String entrantID;

    /**
     * Instantiate a new instance of the fragment
     * @param eventID id for the event that the entrant has been accepted for
     * @param entrantID id for the entrant profile
     * @return an instance of the fragment
     */
    public static InviteFromDetailsFragment newInstance(String eventID, String entrantID) {
        Bundle args = new Bundle();
        args.putString("eventID", eventID);
        args.putString("entrantID", entrantID);
        InviteFromDetailsFragment fragment = new InviteFromDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_alert_popup, null);
        Bundle args = getArguments();
        eventID = args.getString("eventID");
        entrantID = args.getString("entrantID");
        TextView alert_text = view.findViewById(R.id.alert_text);
        TextView message_text = view.findViewById(R.id.message_text);
        Button positive_button = view.findViewById(R.id.postive_button);
        Button negative_button = view.findViewById(R.id.negative_button);

        alert_text.setText("Congratulations!");
        message_text.setText("You have been selected for this event");
        negative_button.setText("Decline");
        positive_button.setText("Accept");

        // Creates fragment with buttons
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(),R.style.MaterialAlertDialog_NoRoundedCorners);
        builder.setView(view);
        Dialog dialog = builder.create();
        positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When User accepts, User will be added to accepted list
                EventDAL DAL = new EventDAL();
                DAL.getEvent(eventID, new EventDAL.OnEventRetrievedListener() {
                    @Override
                    public void onEventRetrieved(Event event){
                        if (event != null) {
                            if (positive_button.getText() == "Accept") {
                                event.addToAcceptedList(entrantID);
                                event.removeFromPendingList(entrantID);
                                DAL.updateEvent(event);
                            }
                        } else {
                            System.err.println("Add to Waitlist Failed");
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        negative_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - Implement Declining Selection and Removing from waitlist
                // When User declines, User will be removed from waiting list
                EventDAL DAL = new EventDAL();
                DAL.getEvent(eventID, new EventDAL.OnEventRetrievedListener() {
                    @Override
                    public void onEventRetrieved(Event event){
                        if (event != null) {
                            if (negative_button.getText() == "Decline"){
                                event.removeFromPendingList(entrantID);
                                event.addToDeclinelist(entrantID);
                            } else {
                                event.removeFromWaitlist(entrantID);
                            }

                            DAL.updateEvent(event);
                        } else {
                            System.err.println("Remove from Waitlist Failed");
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
