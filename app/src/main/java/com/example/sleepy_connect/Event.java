package com.example.sleepy_connect;

import java.util.List;

public class Event {
    public String eventName;
    public String Description;
    public String CommunityCentre;
    public String communityCenter;
    public String communityCenterLocation;
    public String QRCode;
    public String creatorID;
    public String createdDate;
    public String poster; // TODO: Figure out how to store the poster (probably file reference in firebase)
    public String registrationOpens;
    public String registrationCloses;
    public Integer capacity;
    public List<String> waitingList; // List of entrants
    public List<String> pendingList; // List of invited entrants
    public List<String> declinedList; // List of entrants that declines / were rejected
    public List<String> acceptedList; // List of accepted entrants

    public Event(String eventName, String Description) {
        /*title, photo(optional), start date, end date, start time, end time,
        Registration start date, registration end date, description, geolocation toggle,
        event capacity, waitlist capacity(optional), rec center, rec center location*/
    }
}
