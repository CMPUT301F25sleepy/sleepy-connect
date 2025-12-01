package com.example.sleepy_connect;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * class for the Data Access Layer for the community centre objects to the database
 */
public class CommunityCentreDAL {
    private final CollectionReference communityRef;

    /**
     * Stores all functions that interface with the database for the community centre.
     */
    public CommunityCentreDAL() {
        /* Community Centre Data Access Layer - Stores all functions that interface with the database for the community centre. */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Creating a collection for community centres
        communityRef = db.collection("community centres");
    }

    // Constructor that doesn't use firebase FOR TESTS ONLY
    public CommunityCentreDAL(CollectionReference mockRef) {
        this.communityRef = mockRef;
    }

    /**
     * adds a community centre to the collection
     * @param communityCentre object to be added
     */
    public void addCommunityCentre(CommunityCentre communityCentre) {
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

    /**
     * Remove a community centre from the collection
     * @param communityCentre object to be removed
     */
    public void removeCommunityCentre(CommunityCentre communityCentre) {
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

    /**
     * retrieves a community centre from the database
     * @param communityCentreName String centreID of a community centre
     * @param listener CommunityCentre object from database corresponding to the centreID
     */
    public void getCommunityCentre(String communityCentreName, OnCommunityCentreRetrievedListener listener) {
         //Uses a listener to access the data. Please implement the interface (OnCommunityCentreRetrievedListener) when using this.
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

    /**
     * Interface for the callback from getCommunityCentre
     */
    public interface OnCommunityCentreRetrievedListener {
        //UI layer should use this in calls
        void onCommunityCentreRetrieved(CommunityCentre communityCentre);
    }

    /**
     * Updates the corresponding community centre object in Firebase.
     * @param communityCentre CommunityCentre object that was changed in the app
     */
    public void updateCommunityCentre(CommunityCentre communityCentre) {
        // When you make changes locally, it doesn't sync to Firebase unless you call this after.
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

    /**
     * Gets all community centres from Firebase.
     * @param listener for list retrieval
     */
    public void getCommunityCentres(OnCommunityCentresRetrievedListener listener) {
         //Output: List<CommunityCentre> returned in listener
        communityRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    List<CommunityCentre> centres = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        CommunityCentre centre = document.toObject(CommunityCentre.class);
                            centres.add(centre);
                    }
                    listener.onCommunityCentresRetrieved(centres);                          // When the whole query is done
                    System.out.println("got community centres.");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error: " + e.getMessage());
                    listener.onCommunityCentresRetrieved(new ArrayList<>());
                });
    }

    /**
     * Listener for list retrieval
     */
    public interface OnCommunityCentresRetrievedListener {
        void onCommunityCentresRetrieved(List<CommunityCentre> centres);
    }
}