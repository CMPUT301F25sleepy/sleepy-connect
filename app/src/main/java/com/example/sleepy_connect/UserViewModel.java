package com.example.sleepy_connect;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
