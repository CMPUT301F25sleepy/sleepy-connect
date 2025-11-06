package com.example.sleepy_connect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

// Making use of the alertSelectFragment as the functions are similar
public class NotifyCancelled extends alertSelectFragment {

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

        // If the user is cancelled, send the notification with these texts
        if (notif.isCancelled()){
            alert_text.setText("Cancelled");
            message_text.setText("Unfortunately, you have been cancelled from this event");
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

