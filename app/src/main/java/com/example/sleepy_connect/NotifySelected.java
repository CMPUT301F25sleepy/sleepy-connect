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

public class NotifySelected extends alertSelectFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_alert_popup, null);

        Bundle args = getArguments();
        Notification notif = (Notification) args.getSerializable("notification");

        TextView alert_text = view.findViewById(R.id.alert_text);
        TextView message_text = view.findViewById(R.id.message_text);
        Button positive_button = view.findViewById(R.id.postive_button);
        Button negative_button = view.findViewById(R.id.negative_button);

        // If this is a “selected” notification
        assert notif != null;
        if (notif.isSelected()) {
            alert_text.setText("You're Selected!");
            message_text.setText("You have been moved from the waitlist into the pending invite list for:\n\n" + notif.getEvent_name());
        }

        MaterialAlertDialogBuilder builder =
                new MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_NoRoundedCorners);

        builder.setView(view);
        Dialog dialog = builder.create();

        positive_button.setOnClickListener(v -> {
            // TODO: Accept invitation maybe
            dialog.dismiss();
        });

        negative_button.setOnClickListener(v -> {
            // TODO: Decline invitation maybe
            dialog.dismiss();
        });

        return dialog;
    }
}
