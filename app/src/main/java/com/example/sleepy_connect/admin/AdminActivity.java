package com.example.sleepy_connect.admin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sleepy_connect.R;
import com.example.sleepy_connect.admin.profilemanagement.AdminProfileListFragment;

/**
 * main activity for the admin view
 */
public class AdminActivity extends AppCompatActivity {

    public String currentUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        // retrieve current user data
        currentUID = getIntent().getStringExtra("uid");

        // set admin profile list as default child fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_container, AdminProfileListFragment.class, null)
                .commit();

        // set listener for exit button
        TextView tvExit = findViewById(R.id.admin_tv_exit);
        tvExit.setOnClickListener(v -> {
            finish();
        });

        // set listener for dropdown menu click
        TextView tvMenu = findViewById(R.id.admin_tv_list_label);
        tvMenu.setOnClickListener(v -> {

            // open bottom sheet
            AdminBottomSheet bottomSheet = new AdminBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");

        });
    }
}