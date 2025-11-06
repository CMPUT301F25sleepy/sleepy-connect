package com.example.sleepy_connect;

import java.io.Serializable;

public class Notification implements Serializable {
    public String event_name;
    public boolean selected;
    public boolean cancelled;

    public Notification(String event_name, boolean selected){
        this.event_name = event_name;
        this.selected = selected;
        this.cancelled = false;
    }

    public Notification(String event_name, boolean selected, boolean cancelled){
        this.event_name = event_name;
        this.selected = selected;
        this.cancelled = cancelled;
    }


    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public boolean isSelected() {
        return selected;
    }

    // Added isCancelled() for my NotifyCancelled class
    public boolean isCancelled(){ return cancelled; }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
