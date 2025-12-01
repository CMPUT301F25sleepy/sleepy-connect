package com.example.sleepy_connect;

import java.util.List;

/**
 * Class which allows an event creator to manually remove a user from the list of invited entrants for a particular event
 */
public class CancelEntrant {

    private final EventDAL eventDAL;

    // adding dependency injection for tests
    public CancelEntrant(EventDAL eventDAL) {
        this.eventDAL = eventDAL;
    }
    public void removeCancelledEntrant(Entrant entrant, Event event) {
        String entrantID = entrant.getAndroid_id();
        List<String> invitedList = event.getPendingList();

        // Remove from pending list
        if (invitedList.contains(entrantID)) {
            invitedList.remove(entrantID);

            // Sends cancelled notification
            Notification notif = new Notification(
                    event.getEventName(),    // event name
                    false,                   // selected = false
                    true,                    // cancelled = true
                    event.getEventID()       // eventID
            );
            notif.sendNotification(entrantID);
        }

        // Update Firebase event object
        eventDAL.updateEvent(event);
    }
}

