package com.example.sleepy_connect;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Object class for events
 */

public class Event implements Serializable {
    public String eventID;                                  // Automatic
    public String eventName;                                // Required
    public String description;                              // Optional
    public CommunityCentre communityCentre;                 // Required. Create it by using the class.
    public String qrCode;                                   // TODO: Generated automatically
    public String creatorID;                                // Required. Get it by calling entrant.getAndroidID()
    public long createdDate;                                // Automatic timestamp
    public String poster;                                    // Optional. Default -> Default image
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
            String eventID,
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
        this.eventID = eventID;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String base64String) {
        this.poster = base64String;
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

    public void setGeolocationEnabled(boolean geolocationEnabled) {this.geolocationEnabled = geolocationEnabled;}

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

    public void setWaitingList(ArrayList<String> waitingList) {
        this.waitingList = waitingList;
    }

    public void setAcceptedList(ArrayList<String> acceptedList) {
        this.acceptedList = acceptedList;
    }

    public void setPendingList(ArrayList<String> pendingList) {
        this.pendingList = pendingList;
    }

    /**
     * add an entrant to the waitlist for a particular event
     * @param entrantID Entrant to be added
     */
    public void addToWaitlist(String entrantID){
        if (this.waitingList == null){
            this.waitingList = new ArrayList<>();
        }
        this.waitingList.add(entrantID);
    }

    /**
     * add an entrant to the declined list for a particular event after they decline their invitation
     * @param entrantID Entrant to be added
     */
    public void addToDeclinelist(String entrantID){
        if (this.declinedList == null){
            this.declinedList = new ArrayList<>();
        }
        this.declinedList.add(entrantID);
    }

    public void addTolist(String entrantID){
        if (this.waitingList == null){
            this.waitingList = new ArrayList<>();
        }
        this.waitingList.add(entrantID);
    }

    /**
     * remove an entrant from the waitlist for a particular event
     * @param entrantID Entrant to be removed
     */
    public void removeFromWaitlist(String entrantID){
        if (this.waitingList.contains(entrantID)){
            this.waitingList.remove(entrantID);
        } else {
            System.err.println("Error entrantID is not in waiting list");
        }
    }

    /**
     * remove an entrant from the accepted list for a particular event after they have accepted the invitation/are enrolled
     * @param entrantID Entrant to be removed
     */
    public void removeFromAcceptedList(String entrantID){
        if (this.acceptedList.contains(entrantID)){
            this.acceptedList.remove(entrantID);
        } else {
            System.err.println("Error entrantID is not in accepted list");
        }
    }

    /**
     * remove an entrant from the declined list for a particular event
     * @param entrantID Entrant to be removed
     */
    public void removeFromDeclinedList(String entrantID){
        if (this.declinedList.contains(entrantID)){
            this.declinedList.remove(entrantID);
        } else {
            System.err.println("Error entrantID is not in declined list");
        }
    }

    /**
     * remove an entrant form the pending list for a particular event after they accept or decline or to revoke their invitation
     * @param entrantID Entrant to be removed
     */
    public void removeFromPendingList(String entrantID){
        if (this.pendingList.contains(entrantID)){
            this.pendingList.remove(entrantID);
        } else {
            System.err.println("Error entrantID is not in accepted list");
        }
    }

    /**
     * add an entrant to the accepted list for a particular event after they accept their invitation
     * @param entrantID Entrant to be added
     */
    public void addToAcceptedList(String entrantID){
        if (this.acceptedList == null){
            this.acceptedList = new ArrayList<>();
        }
        this.acceptedList.add(entrantID);
    }

    public void setDeclinedList(ArrayList<String> declinedList) {
        this.declinedList = declinedList;
    }

    @Exclude
    public int getWaitlistSize() {
        return waitingList.size();
    }

    /**
     * Returns the event object after erasing records of deleted user in entrant lists.
     * @param event Event object to be updated.
     * @param uid User id to be removed from records.
     * @return Updated event object.
     */
    public static Event removeFromEventLists(Event event, String uid) {

        // update all lists
        event.getWaitingList().remove(uid);
        event.getPendingList().remove(uid);
        event.getAcceptedList().remove(uid);
        event.getDeclinedList().remove(uid);

        return event;
    }

}
