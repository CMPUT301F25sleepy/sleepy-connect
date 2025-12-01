package com.example.sleepy_connect;

import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.sleepy_connect.eventdetails.EventDetailsFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.auth.User;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * Fragment to display notification details. Accessed by clicking on a notification
 */
public class alertSelectFragment extends DialogFragment {
    /* fragment for when a user clicks on notification */
    public String eventID;
    public String entrantID;
    public ArrayList<Notification> notif_list;
    public UserViewModel userVM;
    private FragmentManager fm;
    private Notification notif;

    /**
     * Factory method to instantiate a fragment
     * @param notif the notification it is showing the details for
     * @param entrantID the user identification
     * @return an instance of the fragment
     */
    static alertSelectFragment newInstance(Notification notif, String entrantID, Integer position){
        // creates a new fragment with selected notification as it's argument and return it
        Bundle args =  new Bundle();
        args.putSerializable("notification", notif);
        args.putString("entrantID", entrantID);
        args.putInt("position",position);
        alertSelectFragment fragment = new alertSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userVM = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        fm = requireActivity().getSupportFragmentManager();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        /* Creates pop up screen for fragment.*/

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_alert_popup, null);
        Bundle args = getArguments();
        notif = (Notification) args.getSerializable("notification");
        int position = args.getInt("position");
        entrantID = args.getString("entrantID");
        eventID = notif.getEventID();
        notif_list = userVM.getUser().getValue().getNotification_list();
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

        // since it got selected, it will counted as read
        notif.setRead(true);

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
                                // modify corresponding lists
                                event.addToAcceptedList(entrantID);
                                event.removeFromPendingList(entrantID);

                                // pop-up message after accepting
                                Context ctx = getContext();
                                if (ctx != null) {
                                    Toast.makeText(ctx, "Accepted Invitation", Toast.LENGTH_SHORT).show();
                                }
                            }
                            // remove notification from database
                            removeNotificationFromUser(notif);

                            // update event in database
                            DAL.updateEvent(event);
                        } else {
                            System.err.println("Add to Waitlist Failed");
                        }
                        handleDismiss();
                    }
                });

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
                                Context ctx = getContext();
                                if (ctx != null) {
                                    Toast.makeText(ctx, "Declined Invitation", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (negative_button.getText() == "Opt out"){
                                event.removeFromWaitlist(entrantID);
                                Context ctx = getContext();
                                if (ctx != null) {
                                    Toast.makeText(ctx, "Opted out of Waitlist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            removeNotificationFromUser(notif);
                            DAL.updateEvent(event);

                        } else {
                            System.err.println("Remove from Waitlist Failed");
                        }
                        handleDismiss();
                    }
                });

            }
        });

        return dialog;
    }

    public void updateUserNotifList(ArrayList<Notification> array){
        Entrant user = userVM.getUser().getValue();
        assert user != null;
        user.setNotification_list(array);
        userVM.setUser(user);

        // update database
        EntrantDAL db = new EntrantDAL();
        db.updateEntrant(user);
    }

    public void restartFragment(){
        if (!isAdded()) return;
        FragmentActivity activity = getActivity();
        if (activity == null) return;

        fm = activity.getSupportFragmentManager();
        AlertFragment fragment = AlertFragment.newInstance(notif_list, eventID);
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public void handleDismiss(){
        // sending result back to alertFragment
        if (isAdded()) {
            Bundle result = new Bundle();
            getParentFragmentManager().setFragmentResult("key", result);
        }
        dismiss();
    }

    private void removeNotificationFromUser(Notification notif) {
        Entrant user = userVM.getUser().getValue();
        if (user == null) return;

        ArrayList<Notification> currentList = user.getNotification_list();
        if (currentList == null) return;

        ArrayList<Notification> updatedList = new ArrayList<>(currentList);

        boolean removed = updatedList.remove(notif);
        if (!removed) {
            return;
        }

        // Update ViewModel
        user.setNotification_list(updatedList);
        userVM.setUser(user);

        // Update Firestore user
        EntrantDAL db = new EntrantDAL();
        db.updateEntrant(user);
    }

}
