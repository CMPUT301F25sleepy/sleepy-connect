package com.example.sleepy_connect;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class Event {
    public String eventID;                                  // Automatic
    public String eventName;                                // Required
    public String description;                              // Optional
    public CommunityCentre communityCentre;                 // Required. Create it by using the class.
    public String qrCode;                                   // TODO: Generated automatically
    public String creatorID;                                // Required. Get it by calling entrant.getAndroidID()
    public long createdDate;                                // Automatic timestamp
    public Image poster;                                    // Optional. Default -> Default image
    public long registrationOpens;                          // Required
    public long registrationCloses;                         // Required
    public long eventStartDate;                             // Required
    public long eventEndDate;                               // Required
    public String eventTime;                                // Required. String time of event to display
    public int eventCapacity;                               // Required
    public int waitlistCapacity;                            // Optional. Default -> No limit
    public boolean geolocationEnabled;                      // Required
    public String eventDayOfWeek;                           // Automatic. Calculates string for the day of the week on which the event occurs.
    public ArrayList<String> waitingList;                   // Automatic. List of entrant android IDs
    public ArrayList<String> pendingList;                   // Automatic. List of invited entrant android IDs
    public ArrayList<String> declinedList;                  // Automatic. List of entrants that declines / were rejected by android ID
    public ArrayList<String> acceptedList;                  // Automatic. List of accepted entrant android IDs

    public Event(
            String eventName,
            CommunityCentre communityCentre,
            String creatorID,
            long registrationOpens,
            long registrationCloses,
            long eventStartDate,
            long eventEndDate,
            String eventTime,
            int eventCapacity,
            boolean geolocationEnabled
    )
    /* You need to set the optional fields manually after creating the event using setters. */
    {
        // Required
        this.eventName = eventName;
        this.communityCentre = communityCentre;
        this.creatorID = creatorID;
        this.registrationOpens = registrationOpens;
        this.registrationCloses = registrationCloses;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventTime = eventTime;
        this.eventCapacity = eventCapacity;
        this.geolocationEnabled = geolocationEnabled;

        // Optional
        this.description = null;
        this.poster = null;
        this.waitlistCapacity = Integer.MAX_VALUE;

        // Automatic
        this.eventID = null;
        this.qrCode = null;
        this.createdDate = Instant.now().toEpochMilli();
        this.eventDayOfWeek = Instant.ofEpochMilli(eventStartDate)
                .atZone(ZoneId.systemDefault())
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        this.waitingList = new ArrayList<>();
        this.pendingList = new ArrayList<>();
        this.declinedList = new ArrayList<>();
        this.acceptedList = new ArrayList<>();
    }

    public Event() {
        // Empty constructor for Firebase to use when retrieving stuff
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CommunityCentre getCommunityCentre() {
        return communityCentre;
    }

    public String getQrCode() {
        return qrCode;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public long getCreatedDate() {
        return createdDate;
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

    public long getRegistrationCloses() {
        return registrationCloses;
    }

    public long getEventStartDate() {
        return eventStartDate;
    }

    public long getEventEndDate() {
        return eventEndDate;
    }

    public int getEventCapacity() {
        return eventCapacity;
    }

    public int getWaitlistCapacity() {
        return waitlistCapacity;
    }

    public void setWaitlistCapacity(int waitlistCapacity) {
        this.waitlistCapacity = waitlistCapacity;
    }

    public boolean isGeolocationEnabled() {
        return geolocationEnabled;
    }

    public String getEventTime() {
        return eventTime;
    }

    public ArrayList<String> getPendingList() {
        return pendingList;
    }

    public ArrayList<String> getWaitingList() {
        return waitingList;
    }

    public ArrayList<String> getDeclinedList() {
        return declinedList;
    }

    public ArrayList<String> getAcceptedList() {
        return acceptedList;
    }

    public String getEventDayOfWeek() {
        return eventDayOfWeek;
    }
}
