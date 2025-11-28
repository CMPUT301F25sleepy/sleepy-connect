package com.example.sleepy_connect;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

/**
 * class for view model to get user object
 */
public class UserViewModel extends ViewModel {
    private static final MutableLiveData<Entrant> user = new MutableLiveData<>();

    public MutableLiveData<Entrant> getUser() {
        return user;
    }

    public void setUser(Entrant newUser) {
        user.setValue(newUser);
    }
}
