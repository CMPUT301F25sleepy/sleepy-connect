package com.example.sleepy_connect.eventdetails;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepy_connect.Event;

public class EventViewModel extends ViewModel {
    private static final MutableLiveData<Event> event = new MutableLiveData<>();

    public static MutableLiveData<Event> getEvent() {
        return event;
    }

    public void setEvent(Event newEvent) {
        event.setValue(newEvent);
    }
}
