package com.example.sleepy_connect.eventmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.entrantmanagement.ListViewModel;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.UserViewModel;
import com.example.sleepy_connect.EventViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventManagerFragment extends Fragment {

    private ListView listview;
    private EventManagerListAdapter adapter;
    private Entrant user;
    private final List<Event> eventList = new ArrayList<>();

    public EventManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EventMangerFragment.
     */
    public static EventManagerFragment newInstance() { return new EventManagerFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_manager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set click listener for create event
        Button newEventButton = view.findViewById(R.id.new_event_button);
        newEventButton.setOnClickListener(v-> openCreateEvent());

        // initialize listview and adapter
        listview = view.findViewById(R.id.event_manager_lv_events);
        adapter = new EventManagerListAdapter(eventList);
        listview.setAdapter(adapter);

        // get user info
        user = UserViewModel.getUser().getValue();

        // fetch and display events created by the user
        fetchEventsCreated();

        // set click listener for list view item
        listview.setOnItemClickListener((parent, view1, position, id) -> {
            Event selectedEvent = eventList.get(position);
            fetchEntrantFromList(selectedEvent);

            // pass selected event to viewmodel
            EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
            vmEvent.setEvent(selectedEvent);

            // open bottom sheet
            EventManagerBottomSheet bottomSheet = EventManagerBottomSheet.newInstance(selectedEvent);
            bottomSheet.show(requireActivity().getSupportFragmentManager(), "ModalBottomSheet");
        });
    }

    /**
     * method to open the fragment to create a new event
     */
    private void openCreateEvent() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, CreateEventFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    /**
     * access the database to get the events which the user has created in the past
     */
    public void fetchEventsCreated() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the community centre
        db.collection("events")
                .whereEqualTo("creatorID", user.getAndroid_id())
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    // add queried events to cleared event list
                    eventList.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        eventList.add(doc.toObject(Event.class));
                    }
                    adapter.notifyDataSetChanged();

                });
    }

    private void fetchEntrantFromList(Event event){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // get entrants
        db.collection("events").document(event.getEventID())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Log.e("EntrantManagerFragment", "events broke)");
                        return;
                    }

                    //Find all entrants in the waiting list and put them inside the vm list
                    ArrayList<Entrant> waitingList = new ArrayList<>();
                    ArrayList<Entrant> pendingList = new ArrayList<>();
                    ArrayList<Entrant> acceptedList = new ArrayList<>();
                    ArrayList<Entrant> declinedList = new ArrayList<>();

                    //Gets all entrants in waiting list
                    List<String> WaitEntrantIDs = (List<String>) documentSnapshot.get("waitingList");
                    if (WaitEntrantIDs == null || WaitEntrantIDs.isEmpty()) {
                        Log.d("EntrantManagerFragment", "No entrants in the waiting list of" + event.getEventName());
                    }

                    Log.d("EventListFragment", "got " + WaitEntrantIDs.size() + " entrant ids in the waiting list of" + event.getEventName());

                    for (String id : WaitEntrantIDs) {
                        db.collection("users").document(id)
                                .get()
                                .addOnSuccessListener(entrantDoc -> {
                                    if (entrantDoc.exists()) {
                                        Entrant entrant = entrantDoc.toObject(Entrant.class);
                                        waitingList.add(entrant);
                                        // idk if i need to add adapter
                                        Log.d("EntrantFragment", "Got entrant " + id + " for waiting list");
                                    }
                                })
                                .addOnFailureListener(e ->
                                        Log.e("EntrantFragment", "Something broke with entrant ;-;"));
                    }

                    //Gets all entrants in pending list
                    List<String> pendingEntrantIDs = (List<String>) documentSnapshot.get("pendingList");
                    if (pendingEntrantIDs == null || pendingEntrantIDs.isEmpty()) {
                        Log.d("EntrantManagerFragment", "No entrants in pendingList of " + event.getEventName());
                    }

                    Log.d("EventListFragment", "got " + pendingEntrantIDs.size() + " entrant ids in pendingList of" + event.getEventName());

                    for (String id : pendingEntrantIDs) {
                        db.collection("users").document(id)
                                .get()
                                .addOnSuccessListener(entrantDoc -> {
                                    if (entrantDoc.exists()) {
                                        Entrant entrant = entrantDoc.toObject(Entrant.class);
                                        pendingList.add(entrant);
                                        // idk if i need to add adapter
                                        Log.d("EntrantFragment", "Got entrant " + id + " for pending list");
                                    }
                                })
                                .addOnFailureListener(e ->
                                        Log.e("EntrantFragment", "Something broke with entrant ;-;"));
                    }

                    //Gets all entrants in declined list
                    List<String> declinedEntrantIDs = (List<String>) documentSnapshot.get("declinedList");
                    if (declinedEntrantIDs == null || declinedEntrantIDs.isEmpty()) {
                        Log.d("EntrantManagerFragment", "No entrants in declined list of" + event.getEventName());
                    }

                    Log.d("EventListFragment", "got " + declinedEntrantIDs.size() + " entrant ids in declined list of" + event.getEventName());

                    for (String id : declinedEntrantIDs) {
                        db.collection("users").document(id)
                                .get()
                                .addOnSuccessListener(entrantDoc -> {
                                    if (entrantDoc.exists()) {
                                        Entrant entrant = entrantDoc.toObject(Entrant.class);
                                        declinedList.add(entrant);
                                        // idk if i need to add adapter
                                        Log.d("EntrantFragment", "Got entrant " + id + " for declined list");
                                    }
                                })
                                .addOnFailureListener(e ->
                                        Log.e("EntrantFragment", "Something broke with entrant ;-;"));
                    }

                    //Gets all entrants in accepted list
                    List<String> acceptedEntrantIDs = (List<String>) documentSnapshot.get("acceptedList");
                    if (acceptedEntrantIDs == null || acceptedEntrantIDs.isEmpty()) {
                        Log.d("EntrantManagerFragment", "No entrants in accepted list of" + event.getEventName());
                    }

                    Log.d("EventListFragment", "got " + acceptedEntrantIDs.size() + " entrant ids in accepted list of" + event.getEventName());

                    for (String id : acceptedEntrantIDs) {
                        db.collection("users").document(id)
                                .get()
                                .addOnSuccessListener(entrantDoc -> {
                                    if (entrantDoc.exists()) {
                                        Entrant entrant = entrantDoc.toObject(Entrant.class);
                                        acceptedList.add(entrant);
                                        // idk if i need to add adapter
                                        Log.d("EntrantFragment", "Got entrant " + id + " for accepted list");
                                    }
                                })
                                .addOnFailureListener(e ->
                                        Log.e("EntrantFragment", "Something broke with entrant ;-;"));
                    }

                    //once all lists filled, we update the vm model
                    ListViewModel vm = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
                    vm.setWaitingList(waitingList);
                    vm.setPendingList(pendingList);
                    vm.setAcceptedList(acceptedList);
                    vm.setDeclinedList(declinedList);
                })
                .addOnFailureListener(e ->
                        Log.e("EntrantFragment", "Something broke"));
    }


    /**
     * a class of a custom list adapter for the custom listview
     */
    private class EventManagerListAdapter extends BaseAdapter {
        private final List<Event> events;

        public EventManagerListAdapter(List<Event> events) {
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
            EventManagerFragment.EventManagerListAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.organizer_event_item_layout, parent, false);
                holder = new EventManagerFragment.EventManagerListAdapter.ViewHolder();
                holder.name = convertView.findViewById(R.id.event_title);
                holder.dates = convertView.findViewById(R.id.event_dates);
                holder.dayOfWeek = convertView.findViewById(R.id.event_dayOfWeek);
                holder.time = convertView.findViewById(R.id.event_times);
                holder.capacityProgress = convertView.findViewById(R.id.capacity_progress);
                holder.capacityProgressBar = convertView.findViewById(R.id.capacity_progress_bar);
                holder.inviteProgress = convertView.findViewById(R.id.invite_progress);
                holder.inviteProgressBar = convertView.findViewById(R.id.invite_progress_bar);
                convertView.setTag(holder);
            } else {
                holder = (EventManagerFragment.EventManagerListAdapter.ViewHolder) convertView.getTag();
            }

            Event event = events.get(position);
            holder.name.setText(event.getEventName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/y", Locale.getDefault());
            String start = dateFormat.format(new Date(event.getEventStartDate()));
            String end = dateFormat.format(new Date(event.getEventEndDate()));
            holder.dates.setText(start + " - " + end);
            holder.dayOfWeek.setText(event.getEventDayOfWeek());
            holder.time.setText(event.getEventTime());

            int eventCapacity = event.getEventCapacity();
            int filledSpots = event.getAcceptedList().toArray().length;
            int capacityPercent = (int) (((double)filledSpots/eventCapacity)*100);
            holder.capacityProgress.setText(filledSpots + "/" + eventCapacity);
            holder.capacityProgressBar.setProgress(capacityPercent);

            int invitedNum = event.getPendingList().toArray().length;
            int inviteTotal = eventCapacity - filledSpots;
            int invitePercent = (int )(((double)invitedNum/inviteTotal)*100);
            holder.inviteProgress.setText(invitedNum + "/" + inviteTotal);
            holder.inviteProgressBar.setProgress(invitePercent);

            return convertView;
        }

        /**
         * a class for the information used by the holder
         */
        class ViewHolder {
            TextView name;
            TextView dates;
            TextView dayOfWeek;
            TextView time;
            TextView capacityProgress;
            ProgressBar capacityProgressBar;
            TextView inviteProgress;
            ProgressBar inviteProgressBar;
        }
    }
}
