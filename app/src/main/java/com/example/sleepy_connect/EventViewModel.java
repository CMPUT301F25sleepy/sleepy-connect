package com.example.sleepy_connect;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * class for the view model to get the event object from the database
 */
public class EventViewModel extends ViewModel {
    private static final MutableLiveData<Event> event = new MutableLiveData<>();

    public MutableLiveData<Event> getEvent() {
        return event;
    }

    public void setEvent(Event newEvent) {
        event.setValue(newEvent);
    }
}
