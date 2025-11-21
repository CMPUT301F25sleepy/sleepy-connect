package com.example.sleepy_connect.admin.profilemanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.UserViewModel;
import com.example.sleepy_connect.admin.AdminActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * Displays profile details and provides admin profile management actions.
 */
public class AdminProfileDetailsFragment extends Fragment {

    private String currentUID;
    private Entrant user;
    private TextView listLabel;

    public AdminProfileDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminProfileDetailsFragment.
     */
    public static AdminProfileDetailsFragment newInstance() {
        return new AdminProfileDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // hide list label
        listLabel = requireActivity().findViewById(R.id.admin_tv_list_label);
        listLabel.setVisibility(View.GONE);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_profile_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // store current user's uid
        AdminActivity host = (AdminActivity) requireActivity();
        currentUID = host.currentUID;

        // retrieve selected entrant details from viewmodel
        user = UserViewModel.getUser().getValue();

        // set user fields
        setProfileFields(view);

        // initialize return button listener
        TextView tvReturn = view.findViewById(R.id.admin_profile_details_tv_return);
        tvReturn.setOnClickListener(v -> finishProcedure());

        // initialize delete button listener
        TextView tvDelete = view.findViewById(R.id.admin_profile_details_tv_delete);
        tvDelete.setOnClickListener(v -> deleteProfile());
    }

    /**
     * Sets the fragment's fields from the selected entrant's data.
     * @param root Fragment root view.
     */
    private void setProfileFields(View root) {

        // set username
        TextView tvUsername = root.findViewById(R.id.admin_profile_details_tv_username);
        tvUsername.setText(Objects.toString(user.getUsername(), ""));   // toString ensures empty string shown if null

        // set first name
        TextView tvFirstName = root.findViewById(R.id.admin_profile_details_tv_first_name);
        tvFirstName.setText(Objects.toString(user.getFirst_name(), ""));

        // set last name
        TextView tvLastName = root.findViewById(R.id.admin_profile_details_tv_last_name);
        tvLastName.setText(Objects.toString(user.getLast_name(), ""));

        // set email
        TextView tvEmail = root.findViewById(R.id.admin_profile_details_tv_email);
        tvEmail.setText(Objects.toString(user.getEmail(), ""));

        // set phone number
        TextView tvPhoneNum = root.findViewById(R.id.admin_profile_details_tv_phone);
        tvPhoneNum.setText(Objects.toString(user.getPhone_number(), ""));
    }

    /**
     * Shows activity's list label and pops fragment off backstack.
     */
    private void finishProcedure() {

        listLabel.setVisibility(View.VISIBLE);
        requireActivity().getSupportFragmentManager()
                .popBackStack();

    }

    /**
     * Deletes entrant in database before exiting the fragment
     */
    private void deleteProfile() {

        // block if profile to be deleted is current user
        if (user.getAndroid_id().equals(currentUID)) {
            Toast.makeText(requireActivity(), "Delete your profile through profile settings", Toast.LENGTH_SHORT)
                    .show();
            finishProcedure();
            return;
        }

        // check each event in collection
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").get()
            .addOnSuccessListener(queryDocumentSnapshots -> {

                // update each event's list if deleted user is in it
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    Event updatedEvent = updateEventLists(Objects.requireNonNull(doc.toObject(Event.class)));
                    doc.getReference().set(updatedEvent);
                }

                // delete profile from collection
                db.collection("users").document(user.getAndroid_id()).delete();

                // exit fragment
                finishProcedure();
            });
    }

    /**
     * Returns the event object after erasing records of deleted user in entrant lists.
     * @param event Event object to be updated.
     * @return Updated event object.
     */
    private Event updateEventLists(Event event) {

        // store uid
        String uid = user.getAndroid_id();

        // update all lists
        event.getWaitingList().remove(uid);
        event.getPendingList().remove(uid);
        event.getAcceptedList().remove(uid);
        event.getDeclinedList().remove(uid);

        return event;
    }
}