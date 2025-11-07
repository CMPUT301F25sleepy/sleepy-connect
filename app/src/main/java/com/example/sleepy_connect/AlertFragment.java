package com.example.sleepy_connect;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String entrantID = "";
    private static final String ARG_PARAM2 = "";
    private ArrayList<Notification> array;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlertFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
           array = (ArrayList<Notification>) getArguments().getSerializable("notifs");
        }
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
        String entrantID = args.getString("entrant");

        // Find the ListView in the fragment's layout
        ListView alertList = view.findViewById(R.id.alert_list);

        // Set the adapter
        AlertAdapter adapter = new AlertAdapter(requireContext(), array);
        alertList.setAdapter(adapter);

        alertList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
                Notification selectedNotif = (Notification) parent.getItemAtPosition(position);
                DialogFragment alertFragment = alertSelectFragment.newInstance(selectedNotif,entrantID);
                alertFragment.show(getParentFragmentManager(), "notification");
            }
        });
    }
}