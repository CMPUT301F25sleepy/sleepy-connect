package com.example.sleepy_connect;

import java.util.ArrayList;

public class Entrant {
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
    public ArrayList<String> all_event_list;                // List of all events that entrant is affiliated with
    public ArrayList<String> created_event_list;            // List of all events that entrant may have created (organizer)

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

}
