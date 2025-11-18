package com.example.sleepy_connect;

//NOT USED

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

/**
 * @deprecated now automatically gives user an id and empty profile instead
 */

public class SignUpFragment extends DialogFragment {

    public interface DialogFragmentListener{
        void goToProfile();
    }

    DialogFragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DialogFragmentListener) {
            listener = (DialogFragmentListener) context;
        } else {
            throw new RuntimeException(context + " must implement DialogFragmentListener");
        }
    }


    public static SignUpFragment newInstance(){

        // creates a new fragment with selected notification as it's argument and return it
        Bundle args =  new Bundle();
        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //get all arguments
        Bundle args = getArguments();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.sign_up_fragment, null);
        TextView main = view.findViewById(R.id.main_text);
        TextView sub = view.findViewById(R.id.sub_text);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.CustomAlertDialog);
        String tag = this.getTag();

        if (Objects.equals(tag, "success")) {
            main.setText("Sucess!");
            sub.setText("You have signed up for the event");

            return builder
                    .setView(view)
                    .setNegativeButton("Return", null)
                    .create();
        } else if (Objects.equals(tag, "failure")) {
            return builder
                    .setView(view)
                    .setNegativeButton("Back", null)
                    .setPositiveButton("Profile", (dialog, which) -> {
                        // replaces current fragment with given fragment
                        listener.goToProfile();
                    })
                    .create();
        } else if (Objects.equals(tag, "already applied")) {
            main.setText("Sorry,");
            sub.setText("You have already signed up for the event");
            return builder
                    .setView(view)
                    .setNegativeButton("Return", null)
                    .create();
        }
        return null;
    }
}
