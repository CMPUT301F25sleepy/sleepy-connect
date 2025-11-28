package com.example.sleepy_connect;

import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

/**
 * Fragment to display notification details. Accessed by clicking on a notification
 */
public class alertSelectFragment extends DialogFragment {
    /* fragment for when a user clicks on notification */
    public String eventID;
    public String entrantID;

    /**
     * Factory method to instantiate a fragment
     * @param notif the notification it is showing the details for
     * @param entrantID the user identification
     * @return an instance of the fragment
     */
    static alertSelectFragment newInstance(Notification notif, String entrantID,ArrayList<Notification> array, AlertAdapter adapter, Integer position){
        // TODO - remove notification from list once user has dealt with it

        // creates a new fragment with selected notification as it's argument and return it
        Bundle args =  new Bundle();
        args.putSerializable("notification", notif);
        args.putString("entrantID", entrantID);
        args.putSerializable("array", array);
        args.putSerializable("adapter", adapter);
        args.putInt("position",position);
        alertSelectFragment fragment = new alertSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        /* Creates pop up screen for fragment.*/

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_alert_popup, null);
        Bundle args = getArguments();
        Notification notif = (Notification) args.getSerializable("notification");
        ArrayList<Notification> notif_list = (ArrayList<Notification>) args.getSerializable("array");
        AlertAdapter adapter = (AlertAdapter) args.getSerializable("adapter");
        Integer position = args.getInt("position");
        entrantID = args.getString("entrantID");
        eventID = notif.getEventID();
        TextView alert_text = view.findViewById(R.id.alert_text);
        TextView message_text = view.findViewById(R.id.message_text);
        Button positive_button = view.findViewById(R.id.postive_button);
        Button negative_button = view.findViewById(R.id.negative_button);

        // Changes text of alert and message depending if user was notified of being selected or not
        if (notif.isSelected()){
            alert_text.setText("Congratulations!");
            message_text.setText("You have been selected for this event");
            negative_button.setText("Decline");
            positive_button.setText("Accept");
        }
        else{
            alert_text.setText("Update");
            message_text.setText("You have not been selected for this event. Would you like to stay in the waiting list?");
            negative_button.setText("Opt out");
            positive_button.setText("Stay");
        }
        if (notif.isCancelled()){
            alert_text.setText("Cancelled");
            message_text.setText("Unfortunately, you have been cancelled for this event");
        }

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
                                notif_list.remove(position);
                                adapter.notifyDataSetChanged();
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
                // When User declines, User will be removed from waiting list
                EventDAL DAL = new EventDAL();
                DAL.getEvent(eventID, new EventDAL.OnEventRetrievedListener() {
                    @Override
                    public void onEventRetrieved(Event event){
                        if (event != null) {
                            if (negative_button.getText() == "Decline"){
                                event.removeFromPendingList(entrantID);
                                event.addToDeclinelist(entrantID);
                            }
                            DAL.updateEvent(event);
                            notif_list.remove(position);
                            adapter.notifyDataSetChanged();
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
