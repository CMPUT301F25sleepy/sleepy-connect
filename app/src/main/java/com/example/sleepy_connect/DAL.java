package com.example.sleepy_connect;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DAL {
    /*Data Access Layer - Stores all functions that interface with the database.*/
    private final FirebaseFirestore db;
    private final CollectionReference usersRef;
    private final CollectionReference eventsRef;
    // For events, make a collection metadata with document eventCounter and field nextID set to 0

    public DAL() {
        // Creating the database
        db = FirebaseFirestore.getInstance();
        // Creating a collection for users
        usersRef = db.collection("users");
        // Creating a collection for events
        eventsRef = db.collection("events");
    }

    // -------------------------- ENTRANT -------------------------- //

    public void addEntrant(Entrant entrant) {
        /*Adding an entrant to users collection.
        * Inputs: An entrant object to add to the collection.*/
        usersRef.document(entrant.getAndroid_id()).set(entrant)
                // Adding listeners that tell us whether it was successful
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Entrant added: " + entrant.getAndroid_id());
                    }
                })
                // Adding listeners that tell us whether it was successful
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error adding entrant: " + e.getMessage());
                    }
                });
    }

    public void removeEntrant(Entrant entrant) {
        /*Removing an entrant from the users collection
        * Inputs: An entrant object to be removed.*/
        usersRef.document(entrant.getAndroid_id()).delete()
                // Adding listeners that tell us whether it was successful
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Entrant removed: " + entrant.getAndroid_id());
                    }
                })
                // Adding listeners that tell us whether it was successful
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error removing entrant: " + e.getMessage());
                    }
                });
    }

    public void getEntrant(String android_id, OnEntrantRetrievedListener listener) {
        /*Retrieves an entrant from the database.
        * Input: String android_id of a user
        * Output: Entrant object from database corresponding to the android_id
        * Uses a listener to access the data. Please implement the interface (OnEntrantRetrievedListener) when using this.*/
        usersRef.document(android_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    // Get a document back
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // The entrant is in the database
                        if (documentSnapshot.exists()) {
                            Entrant entrant = documentSnapshot.toObject(Entrant.class);
                            listener.onEntrantRetrieved(entrant);
                            System.out.println("Entrant retrieved: " + android_id);
                        }
                        // entrant is not in the db
                        else {
                            System.err.println("No entrant found with ID: " + android_id);
                            listener.onEntrantRetrieved(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // Something else went wrong with the request
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error retrieving entrant: " + e.getMessage());
                        listener.onEntrantRetrieved(null);
                    }
                });
    }
    public interface OnEntrantRetrievedListener {
        // Interface for the callback from getEntrant -> UI people, use this in your calls
        void onEntrantRetrieved(Entrant entrant);
    }

    public void updateEntrant(Entrant entrant) {
        /*Updates the corresponding entrant object in the firebase. When you make changes locally,
        * It doesn't sync to firebase unless you call this after.
        * Input: Entrant object that was changed in the app
        * Output: Nothing. Updates the entrant in the Firebase.*/
        usersRef.document(entrant.getAndroid_id()).set(entrant)
                // Adding listeners that tell us whether it was successful
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Entrant updated: " + entrant.getAndroid_id());
                    }
                })
                // Adding listeners that tell us whether it was successful
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error updating entrant: " + e.getMessage());
                    }
                });
    }

    // -------------------------- EVENT -------------------------- //

    public void addEvent(Event event) {
        /* Adds an event object to the database. Generates an event ID by using a document in Firebase
        * Inputs: Event object
        */
        // get DB and ID document references
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference counterRef = db.collection("metadata").document("eventCounter");

        // Overkill, but run a db transaction to add ID to the event.
        db.runTransaction(transaction -> {
                    Long currentID = transaction.get(counterRef).getLong("nextID");
                    if (currentID == null) currentID = 0L; // Android studio wants to check for null
                    long newID = currentID + 1;
                    transaction.update(counterRef, "nextID", newID); // increment in the database

                    event.setEventID((int) newID); // cast long to int before storing
                    eventsRef.document(String.valueOf(newID)).set(event);
                    return null;

                }).addOnSuccessListener(unused ->
                        System.out.println("Event added, ID: " + event.getEventID()))
                .addOnFailureListener(e ->
                        System.err.println("Error adding event: " + e.getMessage()));
    }

}