package com.example.sleepy_connect;

import java.io.Serializable;
import java.util.ArrayList;

public class Notification implements Serializable{
    public String event_name;
    public boolean selected;
    public boolean cancelled;
    public String eventID;

    public Notification() {
        // Empty constructor for Firebase to use when retrieving stuff
    }

    public Notification(String event_name, boolean selected){
        this.event_name = event_name;
        this.selected = selected;
        this.cancelled = false;
    }

    public Notification(String event_name, boolean selected, boolean cancelled, String eventID){
        this.event_name = event_name;
        this.selected = selected;
        this.cancelled = cancelled;
        this.eventID = eventID;
    }

    public void sendNotification(String id){
        EntrantDAL DAL = new EntrantDAL();
        DAL.getEntrant(id, new EntrantDAL.OnEntrantRetrievedListener() {
            @Override
            public void onEntrantRetrieved(Entrant entrant) {
                if (entrant != null) {
                    // if user exists, add notification to their array
                    ArrayList<Notification> notif_list = entrant.getNotification_list();
                    if (notif_list == null){
                        notif_list = new ArrayList<>();
                    }
                    notif_list.add(Notification.this);
                    entrant.setNotification_list(notif_list);
                    DAL.updateEntrant(entrant);

                } else {
                    System.err.println("No entrant found with ID: " + id);
                }
            }
        });
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

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

}
