package com.example.sleepy_connect;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.provider.Settings;
import android.widget.Button;

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

        // navigate to testing activity
        Button navigateToTestingActivityButton = findViewById(R.id.navToTestingActivityButton);
        navigateToTestingActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3. Create an Intent to navigate to the new activity
                Intent intent = new Intent(MainActivity.this, TestingActivity.class);

                // 4. Start the new activity
                startActivity(intent);
            }
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