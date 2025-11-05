package com.example.sleepy_connect;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.time.Instant;

public class MainActivity extends AppCompatActivity{
    public DAL dal;
    public Entrant user;
    public String androidId;
    public ArrayList<Notification> mock_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Boilerplate code
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

        // Setup notification list for testing
        Notification mockAlert1 = new Notification("Event at 2pm", true);
        Notification mockAlert2 = new Notification("Another Event at 2pm", false);
        mock_list.add(mockAlert1);
        mock_list.add(mockAlert2);

        // Testing event creation
        long now = Instant.now().toEpochMilli();

        Event testEvent = new Event(
                "Community BBQ",              // eventName
                "Join us for free burgers and games!",  // Description
                "Clareview Community Centre",           // communityCentre
                "3804 139 Ave NW, Edmonton, AB",        // communityCentreLocation
                "QR12345",                              // QRCode
                androidId,                              // creatorID (this device’s user)
                null,                                   // poster (optional)
                now + 86400000L,                        // registration opens in 1 day
                now + 604800000L,                       // registration closes in 7 days
                now + 691200000L,                       // event starts in 8 days
                now + 699840000L,                       // event ends in 8 days + 1 hour
                100,                                    // eventCapacity
                10,                                     // waitlistCapacity
                true                                    // geolocationEnabled
        );

        dal.addEvent(testEvent);
    }

    /* Listener for sign up */
    public void SignUpPress(View view){
        new SignUpFragment().show(getSupportFragmentManager(),"Sign up");
}