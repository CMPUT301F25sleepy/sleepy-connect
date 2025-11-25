package com.example.sleepy_connect;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sleepy_connect.eventdetails.EventDetailsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // TODO: Rename and change types of parameters
    private String entrantID;

    private ListView WListView;
    private MyEventListAdapter Wadapter;

    private ListView EListView;
    private MyEventListAdapter Eadapter;

    //private EventListFragment.EventListAdapter adapter;
    private final List<Event> WEventList = new ArrayList<>();
    private final List<Event> EEventList = new ArrayList<>();

    private FloatingActionButton scanQRFab;
    private Button test_button;


    private EventDAL eventDAL;

    private Entrant user;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param entrantID ID number of entrant, for navigating to event details page.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String entrantID) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString("entrant", entrantID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entrantID = getArguments().getString("entrant");

            user = UserViewModel.getUser().getValue();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        WListView = view.findViewById(R.id.waitlisted_events_list);
        EListView = view.findViewById(R.id.enrolled_events_list);
        scanQRFab = view.findViewById(R.id.qr_scan_fab);
        scanQRFab.setOnClickListener(v -> openQRCodeScanner());

        test_button = view.findViewById(R.id.test_btn);
        test_button.setOnClickListener(v -> testNav());

        Wadapter = new MyEventListAdapter(WEventList);
        WListView.setAdapter(Wadapter);

        Eadapter = new MyEventListAdapter(EEventList);
        EListView.setAdapter(Eadapter);

        //TODO - differentiate between current events and all events
        fetchEventsForEntrant();

        WListView.setOnItemClickListener((parent, view1, position, id) -> {
            Event selectedEvent = WEventList.get(position);
            Log.d("EventFragment", "Clicked event: " + selectedEvent.getEventName());

            // pass selected event to viewmodel
            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
            vmEvent.setEvent(selectedEvent);

            //set toolbar title
            TextView title = requireActivity().findViewById(R.id.set_title);
            title.setText("Event Details");

            // open event details
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, EventDetailsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });

        EListView.setOnItemClickListener((parent, view1, position, id) -> {
            Event selectedEvent = EEventList.get(position);
            Log.d("EventListFragment", "Clicked event: " + selectedEvent.getEventName());

            // pass selected event to viewmodel
            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
            vmEvent.setEvent(selectedEvent);

            //set toolbar title
            TextView title = requireActivity().findViewById(R.id.set_title);
            title.setText("Event Details");

            // open event details
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, EventDetailsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void fetchEventsForEntrant() {
        //TODO - get events from database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }

    /**
     * Custom adapter for the custom listview of events
     */
    public class MyEventListAdapter extends BaseAdapter {
        private final List<Event> events;

        public MyEventListAdapter(List<Event> events) {
            this.events = events;
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int position) {
            return events.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EventFragment.MyEventListAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.event_item_layout, parent, false);
                holder = new EventFragment.MyEventListAdapter.ViewHolder();
                holder.name = convertView.findViewById(R.id.event_title);
                holder.dates = convertView.findViewById(R.id.event_dates);
                holder.dayOfWeek = convertView.findViewById(R.id.event_dayOfWeek);
                holder.time = convertView.findViewById(R.id.event_times);
                convertView.setTag(holder);
            } else {
                holder = (EventFragment.MyEventListAdapter.ViewHolder) convertView.getTag();
            }

            Event event = events.get(position);
            holder.name.setText(event.getEventName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/y", Locale.getDefault());
            String start = dateFormat.format(new Date(event.getEventStartDate()));
            String end = dateFormat.format(new Date(event.getEventEndDate()));
            holder.dates.setText(start + " - " + end);
            holder.dayOfWeek.setText(event.getEventDayOfWeek());
            holder.time.setText(event.getEventTime());

            return convertView;
        }

        /**
         * class for the attributes changed by the holder
         */
        class ViewHolder {
            TextView name;
            TextView dates;
            TextView dayOfWeek;
            TextView time;
        }
    }

    public void openQRCodeScanner() {
        Log.d("QRCodeScanner", "are we here 1");
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(false);
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setBeepEnabled(false);
        qrCodeLauncher.launch(options);
    }

    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        if(result.getContents() == null) {
            Log.d("QRCodeScanner", "Scan cancelled");
        }

        else {
            String contents = result.getContents();
            Log.d("QRCodeScanner", "contents of scan: " + contents);
            Log.d("QRCodeScanner", "id: " + entrantID);

            // Assuming the format is "some_prefix/event_id"
            String eventId = contents.split("/")[1];

            // open event details
            Log.d("QRCodeScanner", "qr code starting event with id " + eventId);

            EventDetailsFragment fragment = EventDetailsFragment.newInstance(entrantID, eventId);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    });

    public void testNav() {
        EventDetailsFragment fragment = EventDetailsFragment.newInstance(entrantID, "22");
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

}