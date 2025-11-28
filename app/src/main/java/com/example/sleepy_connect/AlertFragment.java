package com.example.sleepy_connect;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sleepy_connect.eventdetails.EventDetailsFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for the alert page accessed from the bottom nav bar
 * A simple {@link Fragment} subclass.
 * Use the {@link AlertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertFragment extends Fragment {

    private Entrant user;
    private ArrayList<Notification> notificationsList = new ArrayList<>();
    private TextView noAlert;
    private AlertAdapter adapter;

    public AlertFragment() {
        // Required empty public constructor
    }

    public static AlertFragment newInstance(@NonNull ArrayList<Notification> notif,String entrantID) {
        AlertFragment fragment = new AlertFragment();
        Bundle args = new Bundle();
        args.putSerializable("notifs", notif);
        args.putString("entrant", entrantID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserViewModel userVM = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        user = userVM.getUser().getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String entrantID = user.getAndroid_id();
        // Find the ListView in the fragment's layout
        ListView alertList = view.findViewById(R.id.alert_list);
        noAlert = view.findViewById(R.id.noAlert);

        // Set the adapter
        adapter = new AlertAdapter(requireContext(), notificationsList);
        Log.d("fetchNotifications", "notification_list size (before fetch) " + notificationsList.size());

        alertList.setAdapter(adapter);

        fetchNotifications();
        Log.d("fetchNotifications", "notification_list size (after fetch) " + notificationsList.size());

        getParentFragmentManager().setFragmentResultListener("key", getViewLifecycleOwner(),
                (requestKey, bundle) -> fetchNotifications()
        );

        fetchNotifications();

        alertList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
                Notification selectedNotif = (Notification) parent.getItemAtPosition(position);
                DialogFragment alertFragment = alertSelectFragment.newInstance(selectedNotif,entrantID,position);
                alertFragment.show(getParentFragmentManager(), "notification");
            }
        });
    }

    public void fetchNotifications(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String entrantID = user.getAndroid_id();
        db.collection("users")
                .document(entrantID)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) {
                        Log.e("", "events broke)");
                        return;
                    }

                    Entrant ent = snapshot.toObject(Entrant.class);
                    if (ent == null) {
                        notificationsList.clear();
                    } else {
                        UserViewModel vmUser = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                        vmUser.setUser(ent);
                        ArrayList<Notification> notif_list = ent.getNotification_list();

                        if (notif_list == null) {
                            notificationsList.clear();
                        } else {
                            notificationsList.clear();
                            notificationsList.addAll(notif_list);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    if (notificationsList.isEmpty()) {
                        noAlert.setVisibility(View.VISIBLE);
                    } else {
                        noAlert.setVisibility(View.GONE);
                    }
                });
    }

    public void refreshFragment(){
        FragmentManager fm = requireActivity().getSupportFragmentManager();

        AlertFragment fragment = AlertFragment.newInstance(notificationsList, user.getAndroid_id());

        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }
}