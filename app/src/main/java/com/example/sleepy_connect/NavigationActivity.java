package com.example.sleepy_connect;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sleepy_connect.databinding.ActivityMainBinding;
import com.example.sleepy_connect.databinding.ActivityNavigationBinding;
import com.example.sleepy_connect.eventdetails.CreateEventFragment;

import java.util.ArrayList;


public class NavigationActivity extends AppCompatActivity {
    /* Handles Navigation between fragments of the app */
    // Bottom Navigation View Implementation Code from https://www.youtube.com/watch?v=jOFLmKMOcK0

    ActivityNavigationBinding binding;
    public ArrayList<Notification> mock_list = new ArrayList<>();

    private void replaceFragment(Fragment fragment) {
        // replaces current fragment with given fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Activity with Community fragment and set the title in top to "Community"
        TextView title = findViewById(R.id.set_title);
        replaceFragment(new CommunityFragment());
        title.setText("Community");

        // Setup notification list for testing
        Notification mockAlert1 = new Notification("Event at 2pm", true);
        Notification mockAlert2 = new Notification("Another Event at 2pm", false);
        mock_list.add(mockAlert1);
        mock_list.add(mockAlert2);


        // Sets up navigation bar to switch between fragments
        binding.bottomNavigationView.setOnItemSelectedListener(item ->{

            if (item.getItemId() == R.id.home_button) {
                title.setText("Home");
                replaceFragment(new CommunityFragment());
            } else if (item.getItemId() == R.id.alert_button){
                title.setText("Alerts");
                replaceFragment(AlertFragment.newInstance(mock_list));
            } else if (item.getItemId() == R.id.create_button) {
                title.setText("Create Event");
                replaceFragment(new EventManagerFragment());
            } else if (item.getItemId() == R.id.event_button) {
                title.setText("My Events");
                replaceFragment(new EventFragment());
            } else if (item.getItemId() == R.id.profile_button) {
                title.setText("Profile");
                replaceFragment(new ProfileFragment());
            }

            return true;

        });

    }
}
