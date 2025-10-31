package com.example.sleepy_connect;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DAL {
    /*Data Access Layer - Stores all functions that interface with the database.*/
    private FirebaseFirestore db;
    private CollectionReference usersRef;

    public DAL() {
        // Creating the database
        db = FirebaseFirestore.getInstance();

        // Creating a collection for users
        usersRef = db.collection("users");
    }

    public void addEntrant(Entrant entrant) {
        /*Adding an entrant to users collection.
        Inputs: An entrant object to add to the collection.*/
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
        Inputs: An entrant object to be removed.*/
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
}
