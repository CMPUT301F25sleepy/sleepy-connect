package com.example.sleepy_connect;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AlertActivity extends AppCompatActivity {
    /* Activity for Alerts (for implementing functionality in UI) */

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_alert_list_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // setting the adapter to the listview and grabbing the ArrayList<Notification> from Intent
        ListView alert_list = findViewById(R.id.alert_list);
        ArrayList<Notification> array = (ArrayList<Notification>) getIntent().getSerializableExtra("mock_list");
        AlertAdapter adapter = new AlertAdapter(this,array);
        alert_list.setAdapter(adapter);

        alert_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // if an item on the list is clicked, show the alertSelectFragment
                Notification selectedNotif = (Notification) adapterView.getItemAtPosition(i);
                alertSelectFragment alertFragment = alertSelectFragment.newInstance(selectedNotif);
                alertFragment.show(getSupportFragmentManager(), "notification");
            }
        });
    }



}
