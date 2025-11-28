package com.example.sleepy_connect;

import androidx.lifecycle.ViewModelProvider;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * object class for notifications/alerts sent to entrants
 */
public class Notification implements Serializable{
    public String event_name;
    public boolean selected;
    public boolean cancelled;
    public String eventID;
    public boolean read;

    public Notification() {
        // Empty constructor for Firebase to use when retrieving stuff
    }

    public Notification(String event_name, boolean selected){
        this.event_name = event_name;
        this.selected = selected;
        this.cancelled = false;
        this.read = false;
    }

    public Notification(String event_name, boolean selected, boolean cancelled, String eventID){
        this.event_name = event_name;
        this.selected = selected;
        this.cancelled = cancelled;
        this.eventID = eventID;
        this.read = false;
    }

    /**
     * "sends" notification by adding the notification to the proper entrant's alert tab
     * @param id entrant id of receiving user
     */
    public void sendNotification(String id){
        EntrantDAL DAL = new EntrantDAL();
        DAL.getEntrant(id, new EntrantDAL.OnEntrantRetrievedListener() {

            /**
             * adds the notification to a list associated with each entrant
             * @param entrant entrant object of receiving user
             */
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

    /**
     * boolean of whether the entrant should received a invite to a list/was selected by the lottery
     * @return bool
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * boolean of whether the user should be sent a notification after their invite is cancelled
     * @return bool
     */
    // Added isCancelled() for my NotifyCancelled class
    public boolean isCancelled(){ return cancelled; }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setRead(boolean bool){
        this.read = bool;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

}
