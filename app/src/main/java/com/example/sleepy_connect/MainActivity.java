package com.example.sleepy_connect;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import java.time.Instant;
import java.util.ArrayList;

/**
 * main activity
 * opens the first screen and instantiates DALs
 */
public class MainActivity extends AppCompatActivity{
    public EntrantDAL entrantDal;
    public EventDAL eventDal;
    public CommunityCentreDAL communityCentreDAL;
    public Entrant user;
    public String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Boilerplate code
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Access to Firebase
        entrantDal = new EntrantDAL();
        eventDal = new EventDAL();
        communityCentreDAL = new CommunityCentreDAL();

        // Retrieve the device ID and create an entrant based on it
        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Get user by id. If user doesn't exist, make a new user.
        entrantDal.getEntrant(androidID, new EntrantDAL.OnEntrantRetrievedListener() {
            @Override
            public void onEntrantRetrieved(Entrant entrant) {
                if (entrant != null) {
                    // existing user
                    user = entrant;

                } else {
                    // new user
                    user = new Entrant(androidID);

                    entrantDal.addEntrant(user);
                }
            }
        });
    }

    /**
     * starts the app by switching to the home screen with the navigation
     * @param view current view
     */
    public void startPress(View view){
        // button to switch to the main app (the navigation activity)
        if (user == null) {
            Toast.makeText(MainActivity.this, "User not loaded in yet. Please wait.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(MainActivity.this, NavigationActivity.class);
        i.putExtra("entrantID", androidID);
        i.putExtra("user", user);
        startActivity(i);
    }
}