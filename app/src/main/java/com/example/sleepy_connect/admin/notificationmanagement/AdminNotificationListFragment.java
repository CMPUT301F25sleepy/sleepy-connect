package com.example.sleepy_connect.admin.notificationmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.Notification;
import com.example.sleepy_connect.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Displays all notifications in the app
 */
public class AdminNotificationListFragment extends Fragment {

    private ArrayList<Notification> notifs;
    private AdminNotificationListAdapter adapter;

    public AdminNotificationListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminNotificationListFragment.
     */
    public static AdminNotificationListFragment newInstance() {
        return new AdminNotificationListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // initialize list, listview, adapter
        notifs = new ArrayList<>();
        adapter = new AdminNotificationListAdapter(requireContext(), notifs);
        ListView listView = view.findViewById(R.id.admin_list_lv);
        listView.setAdapter(adapter);

        // update notif list and notify adapter
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereNotEqualTo("notification_list", 0)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    // add all queried entrants to cleared entrant list
                    notifs.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Entrant user = doc.toObject(Entrant.class);
                        assert user != null;
                        notifs.addAll(user.getNotification_list());
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}