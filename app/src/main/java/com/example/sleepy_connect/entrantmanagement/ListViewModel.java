package com.example.sleepy_connect.entrantmanagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepy_connect.Entrant;

import java.util.ArrayList;

/**
 * class for view model to get user object
 */
public class ListViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Entrant>> waitingList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Entrant>> pendingList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Entrant>> acceptedList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Entrant>> declinedList = new MutableLiveData<>();

    public LiveData<ArrayList<Entrant>> getWaitingList() {
        return waitingList;
    }

    public LiveData<ArrayList<Entrant>> getPendingList() {
        return pendingList;
    }

    public LiveData<ArrayList<Entrant>> getAcceptedList() {
        return acceptedList;
    }

    public LiveData<ArrayList<Entrant>> getDeclinedList() {
        return declinedList;
    }

    public void setWaitingList(ArrayList<Entrant> list) {
        waitingList.setValue(list);
    }

    public void setPendingList(ArrayList<Entrant> list) {
        pendingList.setValue(list);
    }

    public void setDeclinedList(ArrayList<Entrant> list) {
        declinedList.setValue(list);
    }

    public void setAcceptedList(ArrayList<Entrant> list) {
        acceptedList.setValue(list);
    }
}