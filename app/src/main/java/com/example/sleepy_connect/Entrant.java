package com.example.sleepy_connect;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * object class for entrants
 */

public class Entrant implements Serializable {
    /* Creates an entrant object:
    Input: android_id
    Output: Entrant object with: First Name, Last Name, Email, Birthday, Phone, Username initialized to NULL
    */
    public String android_id;
    public String birthday;
    public String email;
    public String phone_number;
    public String username;
    public String last_name;
    public String first_name;
    public ArrayList<Notification> notification_list;

    public ArrayList<String> getCreated_event_list() {
        return created_event_list;
    }

    public ArrayList<String> getAll_event_list() {
        return all_event_list;
    }

    public ArrayList<String> all_event_list;                // List of all events that entrant is affiliated with
    public ArrayList<String> created_event_list;            // List of all events that entrant may have created (organizer)

    public Boolean allow_location;

    public Entrant(String android_id) {
        this.android_id = android_id;
        this.first_name = null;
        this.last_name = null;
        this.email = null;
        this.birthday = null;
        this.phone_number = null;
        this.username = null;

        // These will store notifications and ids of events that entrant is affiliated with in some way
        this.notification_list = new ArrayList<>();
        this.all_event_list = new ArrayList<>();
        this.created_event_list = new ArrayList<>();
    }

    public Entrant() {
        // Empty constructor for Firebase to use when retrieving stuff
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void addCreatedEvent(String eventID) {
        this.created_event_list.add(eventID);
    }

    public Boolean getAllow_Location() {
        return allow_location;
    }

    public void setAllow_location(Boolean allow_location) {
        this.allow_location = allow_location;
    }

    public ArrayList<Notification> getNotification_list() {
        if (notification_list == null){
            notification_list = new ArrayList<>();
        }
        return this.notification_list;
    }

    public void setNotification_list(ArrayList<Notification> notification_list) {
        this.notification_list = notification_list;
    }

    public void addToAllEventList(String eventID){
        (this.all_event_list).add(eventID);
    }

}
