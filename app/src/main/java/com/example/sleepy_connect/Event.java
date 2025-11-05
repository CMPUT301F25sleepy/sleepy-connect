package com.example.sleepy_connect;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Event {
    public String eventID;                                  // Automatic
    public String eventName;                                // Required
    public String description;                              // Optional
    public String communityCentre;                          // Required (Name of host community centre
    public String communityCentreLocation;                  // Required (Address of host community centre as a string)
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
    public List<String> waitingList;                        // Automatic. List of entrant android IDs
    public List<String> pendingList;                        // Automatic. List of invited entrant android IDs
    public List<String> declinedList;                       // Automatic. List of entrants that declines / were rejected by android ID
    public List<String> acceptedList;                       // Automatic. List of accepted entrant android IDs

    public Event(
            String eventName,
            String communityCentre,
            String communityCentreLocation,
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
        this.communityCentreLocation = communityCentreLocation;
        this.creatorID = creatorID;
        this.registrationOpens = registrationOpens;
        this.registrationCloses = registrationCloses;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventTime = eventTime;
        this.eventCapacity = eventCapacity;
        this.geolocationEnabled = geolocationEnabled;

        // Optional
        this.description = "";
        this.poster = null;
        this.waitlistCapacity = Integer.MAX_VALUE;

        // Automatic
        this.eventID = "0";                              // DAL will overwrite when it touches it
        this.qrCode = "";
        this.createdDate = Instant.now().toEpochMilli();
        this.waitingList = new ArrayList<>();
        this.pendingList = new ArrayList<>();
        this.declinedList = new ArrayList<>();
        this.acceptedList = new ArrayList<>();
    }

    public Event() {
        // Empty constructor for Firebase to use when retrieving stuff
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID='" + eventID + '\'' +
                ", eventName='" + eventName + '\'' +
                ", description='" + description + '\'' +
                ", communityCentre='" + communityCentre + '\'' +
                ", communityCentreLocation='" + communityCentreLocation + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", creatorID='" + creatorID + '\'' +
                ", createdDate=" + createdDate +
                ", poster=" + poster +
                ", registrationOpens=" + registrationOpens +
                ", registrationCloses=" + registrationCloses +
                ", eventStartDate=" + eventStartDate +
                ", eventEndDate=" + eventEndDate +
                ", eventTime='" + eventTime + '\'' +
                ", eventCapacity=" + eventCapacity +
                ", waitlistCapacity=" + waitlistCapacity +
                ", geolocationEnabled=" + geolocationEnabled +
                ", waitingList=" + waitingList +
                ", pendingList=" + pendingList +
                ", declinedList=" + declinedList +
                ", acceptedList=" + acceptedList +
                '}';
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

    public String getCommunityCentre() {
        return communityCentre;
    }

    public String getCommunityCentreLocation() {
        return communityCentreLocation;
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

    public List<String> getWaitingList() {
        return waitingList;
    }

    public List<String> getPendingList() {
        return pendingList;
    }

    public List<String> getDeclinedList() {
        return declinedList;
    }

    public List<String> getAcceptedList() {
        return acceptedList;
    }

    public String getEventTime() {
        return eventTime;
    }
}