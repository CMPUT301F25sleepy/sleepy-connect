package com.example.sleepy_connect;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.time.Instant;

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

        // Testing community centre
        CommunityCentre testCommunityCentre = new CommunityCentre("Terwillegar Community Centre", "2051 Leger Rd NW, Edmonton, AB T6R 0R9");

        communityCentreDAL.addCommunityCentre(testCommunityCentre);

        // Testing event creation
        long now = Instant.now().toEpochMilli();

        Event testEvent = new Event(
                "Morning Yoga Workshop",          // eventName (Required)
                testCommunityCentre,                        // Community centre (Required)
                androidID,                                  // creatorID (Required) -> entrant.getAndroidID()
                1730788800000L,                             // registrationOpens (Required) - e.g., Nov 5, 2024
                1731393600000L,                             // registrationCloses (Required) - e.g., Nov 12, 2024
                1731476400000L,                             // eventStartDate (Required) - e.g., Nov 13, 2024
                1731487200000L,                             // eventEndDate (Required) - e.g., Nov 13, 2024
                "10:00 AM - 1:00 PM",                       // eventTime (Required)
                30,                                         // eventCapacity (Required)
                true                                        // geolocationEnabled (Required)
        );

        eventDal.addEvent(testEvent);
    }


    // TODO - Make the user go straight to the navigation
    //  page once their android id has been added to the database
    public void startPress(View view){
        // button to switch to the main app (the navigation activity)
        Intent i = new Intent(MainActivity.this, NavigationActivity.class);
        startActivity(i);
    }

}