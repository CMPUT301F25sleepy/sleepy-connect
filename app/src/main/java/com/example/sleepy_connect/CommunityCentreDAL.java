package com.example.sleepy_connect;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CommunityCentreDAL {
    private final CollectionReference communityRef;

    public CommunityCentreDAL() {
        /* Community Centre Data Access Layer - Stores all functions that interface with the database for the community centre. */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Creating a collection for community centres
        communityRef = db.collection("community centres");
    }

    public void addCommunityCentre(CommunityCentre communityCentre) {
        /* Adding a community centre to their collection.
         * Inputs: A community centre object to add to the collection. */
        communityRef.document(communityCentre.getCommunityCentreName()).set(communityCentre)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Community centre added: " + communityCentre.getCommunityCentreName());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error adding community centre: " + e.getMessage());
                    }
                });
    }

    public void removeCommunityCentre(CommunityCentre communityCentre) {
        /* Removing a community centre from the collection.
         * Inputs: A community centre object to be removed. */
        communityRef.document(communityCentre.getCommunityCentreName()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Community centre removed: " + communityCentre.getCommunityCentreName());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error removing community centre: " + e.getMessage());
                    }
                });
    }

    public void getCommunityCentre(String communityCentreName, OnCommunityCentreRetrievedListener listener) {
        /* Retrieves a community centre from the database.
         * Input: String centreID of a community centre
         * Output: CommunityCentre object from database corresponding to the centreID
         * Uses a listener to access the data. Please implement the interface (OnCommunityCentreRetrievedListener) when using this. */
        communityRef.document(communityCentreName).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            CommunityCentre communityCentre = documentSnapshot.toObject(CommunityCentre.class);
                            listener.onCommunityCentreRetrieved(communityCentre);
                            System.out.println("Community centre retrieved: " + communityCentreName);
                        } else {
                            System.err.println("No community centre found with ID: " + communityCentreName);
                            listener.onCommunityCentreRetrieved(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error retrieving community centre: " + e.getMessage());
                        listener.onCommunityCentreRetrieved(null);
                    }
                });
    }

    public interface OnCommunityCentreRetrievedListener {
        // Interface for the callback from getCommunityCentre -> UI layer should use this in calls
        void onCommunityCentreRetrieved(CommunityCentre communityCentre);
    }

    public void updateCommunityCentre(CommunityCentre communityCentre) {
        /* Updates the corresponding community centre object in Firebase.
         * When you make changes locally, it doesn't sync to Firebase unless you call this after.
         * Input: CommunityCentre object that was changed in the app
         * Output: Nothing. Updates the community centre in Firebase. */
        communityRef.document(communityCentre.getCommunityCentreName()).set(communityCentre)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Community centre updated: " + communityCentre.getCommunityCentreName());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error updating community centre: " + e.getMessage());
                    }
                });
    }
}