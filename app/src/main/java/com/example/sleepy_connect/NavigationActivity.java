package com.example.sleepy_connect;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sleepy_connect.databinding.ActivityNavigationBinding;
import com.example.sleepy_connect.entrantmanagement.EntrantManagerSelectedBottomSheet;
import com.example.sleepy_connect.eventmanager.EventManagerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;


import java.util.ArrayList;


/**
 * Activity for the bottom nav bar and toolbar
 * Switches between all of the main screens accessed through the nav bar
 * Has a container to hold all of the screen fragments
 */
public class NavigationActivity extends AppCompatActivity implements SignUpFragment.DialogFragmentListener, EntrantManagerSelectedBottomSheet.EntrantManagerSelectedBottomSheetListener{
    /* Handles Navigation between fragments of the app */
    // Bottom Navigation View Implementation Code from https://www.youtube.com/watch?v=jOFLmKMOcK0


    ActivityNavigationBinding binding;
    private Entrant user;
    public ArrayList<Notification> notification_list = new ArrayList<>();
    String userID;
    TextView title;
    TextView back;
    private ListenerRegistration notificationListener; // used to detect new notifs
    private int lastNotifCount = -1; // used to check for notification list updates

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

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
        if (getIntent().getStringExtra("notification") != null) {
            title.setText("Alerts");
            replaceFragment(AlertFragment.newInstance(notification_list, userID));
            BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
            navView.setSelectedItemId(R.id.alert_button);
        } else {
            title.setText("Community");
            replaceFragment(CommunityFragment.newInstance(userID));
        }

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
                replaceFragment(EventFragment.newInstance(userID));
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

        //setup notification listener
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        notificationListener = db.collection("users")
                .document(userID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null || snapshot == null || !snapshot.exists()){
                            Log.d("Snapshot Listener for user", "Listener failed");
                            return;
                        }

                        Entrant entrant = snapshot.toObject(Entrant.class);

                        if (entrant == null){
                            Log.d("Snapshot Listener for user", "entrant does not exist");
                            return;
                        }

                        ArrayList<Notification> notif_list = entrant.getNotification_list();

                        if (notif_list == null){
                            Log.d("Snapshot Listener for user", "entrant's notification list does not exist");
                            return;
                        }

                        // first run should initialize size
                        if (lastNotifCount == -1){
                            lastNotifCount = notif_list.size();
                            return;
                        }

                        if (notif_list.size() > lastNotifCount){
                            Notification newNotif = notif_list.get(notif_list.size()-1);
                            DisplayNotification(newNotif);
                        }

                        lastNotifCount = notif_list.size();

                    }
                });
    }

    @Override
    protected void onStop(){
        super.onStop();

        // close notification listener when app closes
        if (notificationListener != null){
            notificationListener.remove();
        }

    }

    @Override
    public void goToProfile() {
        title.setText("Profile");
        replaceFragment(new ProfileFragment());
    }

    public void DisplayNotification(Notification notif){
        // Float Notifications Code from this video: https://www.youtube.com/watch?v=v1s36wmqP8M&t=319s
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = manager.getNotificationChannel("my_channel_id_01");

        String title;
        String description;

        // from the notification object, determine title and description
        if (notif.isSelected()){
            title = "Congrats!";
            description = "You have been invited to " + notif.getEvent_name();
        } else {
            title = "Sorry";
            description = "You were not selected for " + notif.getEvent_name();
        }

        if (channel == null){
            channel = new NotificationChannel("my_channel_id_01", "Channel title", NotificationManager.IMPORTANCE_HIGH);
            //config notification channel
            channel.setDescription("[Channel Description]");
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,1000,200,340});
            channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }

        Intent notifIntent = new Intent(this,NavigationActivity.class);
        notifIntent.putExtra("entrantID", userID);
        notifIntent.putExtra("user", user);
        notifIntent.putExtra("notification","This is to let activity know this is coming from notifications");
        notifIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notifIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"my_channel_id_01")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100,1000,200,340})
                .setAutoCancel(true)
                .setTicker("Notification");

        builder.setContentIntent(contentIntent);
        NotificationManagerCompat m = NotificationManagerCompat.from(getApplicationContext());
        // id to generate new notification in list notifications menu

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            askNotificationPermission();
            return;
        }
        m.notify(1,builder.build());
    }

    @Override
    public void EntrantManagerSelectedBottomSheetClosed(boolean bsClosed, String label, Event event) {

    }


}
