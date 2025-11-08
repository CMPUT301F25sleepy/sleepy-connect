package com.example.sleepy_connect;

//NOT USED

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SignUpFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sign_up_fragment, null);
        EditText editFirstName = view.findViewById(R.id.edit_text_first_name);
        EditText editLastName = view.findViewById(R.id.edit_text_last_name);
        EditText editEmail = view.findViewById(R.id.edit_text_email);
        EditText editBirthday = view.findViewById(R.id.edit_text_birthday);
        EditText editPhone = view.findViewById(R.id.edit_text_phone);
        EditText editUsername = view.findViewById(R.id.edit_text_username);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.CustomAlertDialog);
        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String firstName = editFirstName.getText().toString();
                    String lastName = editLastName.getText().toString();
                    String email = editEmail.getText().toString();
                    String birthday = editBirthday.getText().toString();
                    String phone = editPhone.getText().toString();
                    String username = editUsername.getText().toString();
                    //listener.addEntrant(new Entrant(firstName, lastName, email, birthday, phone, username, password));
                })
                .create();
        }



}
