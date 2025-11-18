package com.example.sleepy_connect;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sleepy_connect.databinding.ActivityNavigationBinding;
import com.example.sleepy_connect.eventmanager.EventManagerFragment;

import java.util.ArrayList;


/**
 * Activity for the bottom nav bar and toolbar
 * Switches between all of the main screens accessed through the nav bar
 * Has a container to hold all of the screen fragments
 */
public class NavigationActivity extends AppCompatActivity implements SignUpFragment.DialogFragmentListener{
    /* Handles Navigation between fragments of the app */
    // Bottom Navigation View Implementation Code from https://www.youtube.com/watch?v=jOFLmKMOcK0

    ActivityNavigationBinding binding;
    private Entrant user;
    public ArrayList<Notification> notification_list = new ArrayList<>();
    String userID;
    TextView title;
    TextView back;

    /**
     *  replaces current fragment with given fragment
     * @param fragment fragment to be switched to
     */
    private void replaceFragment(Fragment fragment) {
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

        // get user
        user = (Entrant) getIntent().getSerializableExtra("user");
        userID = (String) getIntent().getSerializableExtra("entrantID");

        // store user in UserViewModel
        UserViewModel userVM = new ViewModelProvider(this).get(UserViewModel.class);
        userVM.setUser(user);

        // Initialize Activity with Community fragment and set the title in top to "Community"
        title = findViewById(R.id.set_title);
        replaceFragment(CommunityFragment.newInstance(userID));
        title.setText("Community");

        userVM.getUser().observe(this, entrant -> {
            if (entrant == null) {
                notification_list = new ArrayList<>();
                return;
            }

            notification_list = entrant.getNotification_list();
        });


        // Sets up navigation bar to switch between fragments
        binding.bottomNavigationView.setOnItemSelectedListener(item ->{

            if (item.getItemId() == R.id.home_button) {
                title.setText("Community");
                replaceFragment(CommunityFragment.newInstance(userID));
            } else if (item.getItemId() == R.id.alert_button){
                title.setText("Alerts");
                replaceFragment(AlertFragment.newInstance(notification_list, userID));
            } else if (item.getItemId() == R.id.create_button) {
                title.setText("Event Manager");
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

        // initialize back button listener
        back = findViewById(R.id.app_bar_back);
        back.setOnClickListener(v -> {
            getSupportFragmentManager().popBackStack();
        });

    }

    @Override
    public void goToProfile() {
        title.setText("Profile");
        replaceFragment(new ProfileFragment());
    }
}
