package com.example.sleepy_connect;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                    //xander_test(user);

                } else {
                    // new user
                    user = new Entrant(androidID);

                    //xander_test(user);
                    entrantDal.addEntrant(user);
                }
            }
        });

//        // Testing community centre
//        CommunityCentre testCommunityCentre = new CommunityCentre("Terwillegar Community Centre", "2051 Leger Rd NW, Edmonton, AB T6R 0R9");
//
//        communityCentreDAL.addCommunityCentre(testCommunityCentre);
//
//        // Testing community centre
//        CommunityCentre testCommunityCentre1 = new CommunityCentre("All Locations", "See all events");
//
//        communityCentreDAL.addCommunityCentre(testCommunityCentre1);
//
//        // Testing event creation
//        long now = Instant.now().toEpochMilli();
//
//        Event testEvent = new Event(
//                "Morning Yoga Workshop",          // eventName (Required)
//                testCommunityCentre,                        // Community centre (Required)
//                androidID,                                  // creatorID (Required) -> entrant.getAndroidID()
//                1730788800000L,                             // registrationOpens (Required) - e.g., Nov 5, 2024
//                1731393600000L,                             // registrationCloses (Required) - e.g., Nov 12, 2024
//                1731476400000L,                             // eventStartDate (Required) - e.g., Nov 13, 2024
//                1731487200000L,                             // eventEndDate (Required) - e.g., Nov 13, 2024
//                "10:00 AM - 1:00 PM",                       // eventTime (Required)
//                30,                                         // eventCapacity (Required)
//                true                                        // geolocationEnabled (Required)
//        );
//        eventDal.addEvent(testEvent);

    }


    // TODO - Make the user go straight to the navigation
    //  page once their android id has been added to the database

    /**
     * starts the app by switching to the home screen with the navigation
     * @param view current view
     */
    public void startPress(View view){
        // button to switch to the main app (the navigation activity)
        if (user == null) {
            Log.e("DEBUG", "user not loaded in");
            return;
        }
        Intent i = new Intent(MainActivity.this, NavigationActivity.class);
        i.putExtra("entrantID", androidID);
        i.putExtra("user", user);
        startActivity(i);
    }

    /*public void xander_test(Entrant user){
        *//* Adding notifications and user in wishlist to test functionalities of issues *//*

        // testing notification functionality for event "7" in database
        Notification test_notif1 = new Notification("Swimming Lessons at Windermere Recreation Centre", true, false,"7");
        Notification test_notif2 = new Notification("Swimming Lessons at Windermere Recreation Centre", false, false,"7");
        ArrayList<Notification> test_notif_list = new ArrayList<>();
        test_notif_list.add(test_notif1);
        test_notif_list.add(test_notif2);
        user.setNotification_list(test_notif_list);
        entrantDal.updateEntrant(user);

        // testing for waitlist and accepted list for event "7" in database
        eventDal.getEvent("7", new EventDAL.OnEventRetrievedListener() {
            @Override
            public void onEventRetrieved(Event event){
                if (event != null) {
                    // setup waiting list with current user
                    ArrayList<String> test_wishlist = new ArrayList<>();
                    test_wishlist.add(user.android_id);

                    // add use to waiting and pending
                    event.setWaitingList(test_wishlist);
                    event.setPendingList(test_wishlist);

                    // reset accepted and declined list
                    event.setAcceptedList(new ArrayList<>());
                    event.setDeclinedList(new ArrayList<>());

                    eventDal.updateEvent(event);
                } else {
                    System.err.println("Error Event does not exist");
                }
            }
        });

    }*/

}