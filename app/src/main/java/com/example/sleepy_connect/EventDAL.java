package com.example.sleepy_connect;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Data access layer for the event objects to the database
 */
public class EventDAL {
    private final CollectionReference eventsRef;
    private final DocumentReference counterRef;
    // For events, make a collection metadata with document eventCounter and field nextID set to 0

    /**
     * Event Data Access Layer - Stores all functions that interface with the database for events
     */
    public EventDAL() {
        // Creating the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Creating a collection for events
        eventsRef = db.collection("events");
        // Creating a reference to the counter for IDs for events
        counterRef = db.collection("metadata").document("eventCounter");
    }

    /**
     * Adds an event object to the database. Generates an event ID by using a document in Firebase
     * @param event event object
     */
    public void addEvent(Event event) {
        // Get the current nextID, increment it, and store the event
        eventsRef.document(event.getEventID()).set(event) // Create the document
                // Adding listeners that tell us whether it was successful
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Event added: " + event.getEventID());
                    }
                })
                // Adding listeners that tell us whether it was successful
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error with event.");
                    }
                });
    }

    /**
     * Retrieves an event given a string ID.
     * @param eventID string reference to user
     * @param listener Interface for the callback from getEvent
     */
    public void getEvent(String eventID, EventDAL.OnEventRetrievedListener listener) {
        //Outputs: Event object
        eventsRef.document(eventID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    // Get a document back
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // The entrant is in the database
                        if (documentSnapshot.exists()) {
                            Event event = documentSnapshot.toObject(Event.class);
                            listener.onEventRetrieved(event);
                            System.out.println("Event retrieved: " + eventID);
                        }
                        // entrant is not in the db
                        else {
                            System.err.println("No event found with ID: " + eventID);
                            listener.onEventRetrieved(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // Something else went wrong with the request
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error retrieving event: " + e.getMessage());
                        listener.onEventRetrieved(null);
                    }
                });
    }

    /**
     * Interface for the callback from getEvent
     */
    public interface OnEventRetrievedListener {
        //UI people, use this in your calls
        void onEventRetrieved(Event event);
    }

    /**
     * Updates the corresponding event object in the firebase.
     * @param event Event object that was changed in the app
     */
    public void updateEvent(Event event) {
        /* When you make changes locally, it doesn't sync to firebase unless you call this after.
         * Output: Nothing. Updates the entrant in the Firebase.*/
        eventsRef.document(event.getEventID()).set(event)
                // Adding listeners that tell us whether it was successful
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Event updated: " + event.getEventID());
                    }
                })
                // Adding listeners that tell us whether it was successful
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error updating event: " + e.getMessage());
                    }
                });
    }

    /**
     * Uses a document in firebase to get next ID for an event.
     * @param onSuccessListener listener to track updates
     */
    public void getNextID(OnSuccessListener<String> onSuccessListener) {
        counterRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    Long currentID = documentSnapshot.getLong("nextID");
                    if (currentID == null) currentID = 0L;
                    long newID = currentID + 1;

                    // Update counter
                    String finalCurrentID = currentID.toString();                    // Android studio wants this code
                    counterRef.update("nextID", newID)
                            .addOnSuccessListener(aVoid -> {
                                onSuccessListener.onSuccess(finalCurrentID);
                            });
                });
    }

    /**
     * Removing an event from the event collection.
     * @param eventID ID of event object to be removed.
     * @param organizerID ID of given event's organizer.
     */
    public void removeEvent(String eventID, String organizerID) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // update community centres
        Task<QuerySnapshot> updateCommunityCentres =
                db.collection("community centres")
                .whereArrayContains("events", eventID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    // remove reference to event id if community centre has a reference
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        CommunityCentre location = doc.toObject(CommunityCentre.class);
                        assert location != null;
                        location.events.remove(eventID);
                        doc.getReference().set(location);
                    }
                });

        // update organizer
        Task<QuerySnapshot> updateOrganizer =
        db.collection("users")
                .whereEqualTo("android_id", organizerID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    // remove reference in organizer's created event list
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Entrant organizer = doc.toObject(Entrant.class);
                        assert organizer != null;
                        organizer.getCreated_event_list().remove(eventID);
                        doc.getReference().set(organizer);
                    }
                });

        // TODO: update entrants

        // delete from events collection
        eventsRef.document(eventID).delete()
                // Adding listeners that tell us whether it was successful
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Event removed: " + eventID);
                    }
                })
                // Adding listeners that tell us whether it was successful
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error removing event: " + e.getMessage());
                    }
                });

        // synchronize tasks
        ArrayList<Task<?>> removeEventTasks = new ArrayList<>();
        removeEventTasks.add(updateCommunityCentres);
        removeEventTasks.add(updateOrganizer);
        // TODO: add update entrants task
        Task<Void> barrierTask = Tasks.whenAll(removeEventTasks);

        // perform callback on all complete
        barrierTask.addOnCompleteListener(task -> {

            // TODO: add callback here

        });
    }
}
