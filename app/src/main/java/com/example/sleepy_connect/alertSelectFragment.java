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


public class alertSelectFragment extends DialogFragment {
    /* fragment for when a user clicks on notification */

    static alertSelectFragment newInstance(Notification notif){

        // creates a new fragment with selected notification as it's argument and return it
        Bundle args =  new Bundle();
        args.putSerializable("notification", notif);
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
        TextView alert_text = view.findViewById(R.id.alert_text);
        TextView message_text = view.findViewById(R.id.message_text);
        Button positive_button = view.findViewById(R.id.postive_button);
        Button negative_button = view.findViewById(R.id.negative_button);

        // Changes text of alert and message depending if user was notified of being selected or not
        if (notif.isSelected()){
            alert_text.setText("Congratulations!");
            message_text.setText("You have been selected for this event");
        }
        else{
            alert_text.setText("Update");
            message_text.setText("You have not been selected for this event. Would you like to stay in the waiting list?");
            negative_button.setText("Opt out");
        }

        // Creates fragment with buttons
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(),R.style.MaterialAlertDialog_NoRoundedCorners);
        builder.setView(view);
        Dialog dialog = builder.create();
        positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - Implement Accepting Selection
                dialog.dismiss();
            }
        });

        negative_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - Implement Declining Selection and Removing from waitlist
                dialog.dismiss();
            }
        });

        return dialog;
    }


}
