package com.example.sleepy_connect.admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepy_connect.Entrant;

/**
 * View model to store passed user data
 */
public class AdminUserViewModel extends ViewModel {
    private static final MutableLiveData<Entrant> user = new MutableLiveData<>();

    public MutableLiveData<Entrant> getUser() {
        return user;
    }

    public void setUser(Entrant newUser) {
        user.setValue(newUser);
    }
}
