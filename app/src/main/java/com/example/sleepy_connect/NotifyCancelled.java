package com.example.sleepy_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NotifyCancelled extends AppCompatActivity {

    private FirebaseFirestore db;
    private static final String TAG = "MainActivity";
    private static final String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";

    // Replace this with actual FCM server key (from Firebase Console > Project Settings > Cloud Messaging)
    private static final String SERVER_KEY = "key=771930719315";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

        sendNotificationsToAllUsers();
        FirebaseApp.initializeApp(this);
    }

    private void sendNotificationsToAllUsers() {
        CollectionReference usersRef = db.collection("users");
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> androidIds = new ArrayList<>();

                for (DocumentSnapshot doc : task.getResult()) {
                    // Assuming each user doc contains an 'android_id' or FCM token
                    String androidId = doc.getString("android_id");
                    if (androidId != null && !androidId.isEmpty()) {
                        androidIds.add(androidId);
                    }
                }

                Log.d(TAG, "Found " + androidIds.size() + " users");
                for (String id : androidIds) {
                    sendPushNotification(id, "Is this working", "This is a test notification.");
                }

            } else {
                Log.e(TAG, "Error getting users: ", task.getException());
                Toast.makeText(this, "Failed to retrieve users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPushNotification(String androidId, String title, String message) {
        new Thread(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("to", androidId); // The FCM registration token

                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("body", message);

                payload.put("notification", notification);

                URL url = new URL(FCM_API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", SERVER_KEY);
                conn.setRequestProperty("Content-Type", "application/json");

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Scanner scanner = new Scanner(conn.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();
                    Log.d(TAG, "Notification sent to " + androidId + ": " + response);
                } else {
                    Log.e(TAG, "Error sending notification. Response code: " + responseCode);
                }

                conn.disconnect();
            } catch (JSONException e) {
                Log.e(TAG, "JSON Exception: ", e);
            } catch (Exception e) {
                Log.e(TAG, "Error sending FCM message: ", e);
            }
        }).start();
    }
}

