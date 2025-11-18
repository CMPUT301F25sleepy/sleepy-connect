package com.example.sleepy_connect;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Data access layer for the entrant objects to the database
 */
public class EntrantDAL {
    private final CollectionReference usersRef;

    /**
     * Entrant Data Access Layer - Stores all functions that interface with the database for the entrant
     */
    public EntrantDAL() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Creating a collection for users
        usersRef = db.collection("users");
    }

    /**
     * Adding an entrant to users collection.
     * @param entrant An entrant object to add to the collection.
     */
    public void addEntrant(Entrant entrant) {
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

    /**
     * Removing an entrant from the users collection
     * @param entrant An entrant object to be removed.
     */
    public void removeEntrant(Entrant entrant) {
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

    /**
     * Retrieves an entrant from the database.
     * @param android_id String android_id of a user
     * @param listener listener to access the data.
     */
    public void getEntrant(String android_id, OnEntrantRetrievedListener listener) {
        /*Output: Entrant object from database corresponding to the android_id
         *Please implement the interface (OnEntrantRetrievedListener) when using this.*/
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

    /**
     * Interface for the callback from getEntrant
     */
    public interface OnEntrantRetrievedListener {
        //UI people, use this in your calls
        void onEntrantRetrieved(Entrant entrant);
    }

    /**
     * Updates the corresponding entrant object in the firebase.
     * @param entrant Entrant object that was changed in the app
     */
    public void updateEntrant(Entrant entrant) {
        /* When you make changes locally, it doesn't sync to firebase unless you call this after.
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
}