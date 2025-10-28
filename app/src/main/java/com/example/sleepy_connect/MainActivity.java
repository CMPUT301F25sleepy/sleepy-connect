package com.example.sleepy_connect;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements SignUpFragment.SignUpDialogueListener {
    private DAL dal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Boilerplate code
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; // checking
        });

        // Testing firestore yay
        DAL dal = new DAL();
        Entrant e1 = new Entrant("Test1", "Test1", "test1@gmail.com", "2000-01-01", "123", "test1", "password");

        dal.addEntrant(e1);
        //dal.removeEntrant(e1);
        Entrant e2 = new Entrant("test2", "test2", "test2@gmail.com", "2000-01-01", "123", "test2", "password");
        dal.addEntrant(e2);
        //dal.removeEntrant(e2);
    }

    public void SignUpPress(View view){
        new SignUpFragment().show(getSupportFragmentManager(),"Sign up");
    }

    @Override
    public void addEntrant(Entrant entrant){
        dal.addEntrant(entrant);
    }
}