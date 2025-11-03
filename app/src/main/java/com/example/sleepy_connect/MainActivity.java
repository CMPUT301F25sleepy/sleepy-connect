package com.example.sleepy_connect;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import android.provider.Settings;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements SignUpFragment.SignUpDialogueListener{
    public DAL dal;
    public Entrant user;
    public String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Boilerplate code
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Access to Firebase
        dal = new DAL();

        // Retrieve the device ID and create an entrant based on it
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Get user by id. If user doesn't exist, make a new user.
        dal.getEntrant(androidId, new DAL.OnEntrantRetrievedListener() {
            @Override
            public void onEntrantRetrieved(Entrant entrant) {
                if (entrant != null) {
                    // existing user
                    user = entrant;
//                    user.setAccess(30);
//                    dal.updateEntrant(user);
                } else {
                    // new user
                    user = new Entrant(androidId);
                    dal.addEntrant(user);
//                    user.setAccess(45);
//                    dal.updateEntrant(user);
                }
            }
        });

        // This part is for FCM token initialization so that Firestore can send a notification
        // I think Firestore handles this cause it'd be hard to actually identify devices arbitrarily
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String userId = "95531acb940aa984";

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("MainActivity", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Gets new FCM registration token
                    String token = task.getResult();
                    Log.d("MainActivity", "FCM Token: " + token);

                    // Saves the token to Firestore within the userId
                    db.collection("users").document(userId)
                            .set(Collections.singletonMap("token", token), SetOptions.merge())
                            .addOnSuccessListener(aVoid -> Log.d("MainActivity", "Token saved successfully"))
                            .addOnFailureListener(e -> Log.e("MainActivity", "Error saving token", e));
                });
    }

    public void SignUpPress(View view){
        new SignUpFragment().show(getSupportFragmentManager(),"Sign up");
    }

    @Override
    @NonNull
    public void addEntrant(Entrant entrant){
        dal.addEntrant(entrant);
    }
}