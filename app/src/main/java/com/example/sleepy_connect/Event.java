package com.example.sleepy_connect;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Event {
    public String eventID;
    public String eventName;
    public String description;
    public String communityCentre;
    public String communityCentreLocation;
    public String qrCode;
    public String creatorID;
    public long createdDate;
    public Image poster;
    public long registrationOpens;
    public long registrationCloses;
    public long eventStartDate;
    public long eventEndDate;
    public int eventCapacity;
    public int waitlistCapacity;
    public boolean geolocationEnabled;
    public List<String> waitingList; // List of entrants
    public List<String> pendingList; // List of invited entrants
    public List<String> declinedList; // List of entrants that declines / were rejected
    public List<String> acceptedList; // List of accepted entrants

    public Event(String eventName, String description, String communityCentre, String communityCentreLocation,
                 String qrCode, String creatorID, Image poster, long registrationOpens, long registrationCloses,
                 long eventStartDate, long eventEndDate, Integer eventCapacity, Integer waitlistCapacity,
                 boolean geolocationEnabled) {
        /*title, photo(optional), start date, end date, start time, end time,
        Registration start date, registration end date, description, geolocation toggle,
        event capacity, waitlist capacity(optional), rec center, rec center location*/
        this.eventID = "0"; // Will be overwritten after DAL touches it
        this.eventName = eventName;
        this.description = description;
        this.communityCentre = communityCentre;
        this.communityCentreLocation = communityCentreLocation;
        this.qrCode = qrCode;
        this.creatorID = creatorID;
        Instant instant = Instant.now();
        this.createdDate = instant.toEpochMilli(); // Gets current time from user device
        this.poster = poster;
        this.registrationOpens = registrationOpens;
        this.registrationCloses = registrationCloses;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventCapacity = eventCapacity;
        this.waitlistCapacity = waitlistCapacity;
        this.geolocationEnabled = geolocationEnabled;
        this.waitingList = new ArrayList<>();
        this.pendingList = new ArrayList<>();
        this.declinedList = new ArrayList<>();
        this.acceptedList = new ArrayList<>();
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommunityCentre() {
        return communityCentre;
    }

    public void setCommunityCentre(String communityCentre) {
        this.communityCentre = communityCentre;
    }

    public String getCommunityCentreLocation() {
        return communityCentreLocation;
    }

    public void setCommunityCentreLocation(String communityCentreLocation) {
        this.communityCentreLocation = communityCentreLocation;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public Image getPoster() {
        return poster;
    }

    public void setPoster(Image poster) {
        this.poster = poster;
    }

    public long getRegistrationOpens() {
        return registrationOpens;
    }

    public void setRegistrationOpens(long registrationOpens) {
        this.registrationOpens = registrationOpens;
    }

    public long getRegistrationCloses() {
        return registrationCloses;
    }

    public void setRegistrationCloses(long registrationCloses) {
        this.registrationCloses = registrationCloses;
    }

    public long getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(long eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public long getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(long eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public Integer getEventCapacity() {
        return eventCapacity;
    }

    public void setEventCapacity(Integer eventCapacity) {
        this.eventCapacity = eventCapacity;
    }

    public Integer getWaitlistCapacity() {
        return waitlistCapacity;
    }

    public void setWaitlistCapacity(Integer waitlistCapacity) {
        this.waitlistCapacity = waitlistCapacity;
    }

    public boolean isGeolocationEnabled() {
        return geolocationEnabled;
    }

    public void setGeolocationEnabled(boolean geolocationEnabled) {
        this.geolocationEnabled = geolocationEnabled;
    }

    public List<String> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(List<String> waitingList) {
        this.waitingList = waitingList;
    }

    public List<String> getPendingList() {
        return pendingList;
    }

    public void setPendingList(List<String> pendingList) {
        this.pendingList = pendingList;
    }

    public List<String> getDeclinedList() {
        return declinedList;
    }

    public void setDeclinedList(List<String> declinedList) {
        this.declinedList = declinedList;
    }

    public List<String> getAcceptedList() {
        return acceptedList;
    }

    public void setAcceptedList(List<String> acceptedList) {
        this.acceptedList = acceptedList;
    }
}