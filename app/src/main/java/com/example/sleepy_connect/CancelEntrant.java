package com.example.sleepy_connect;

import java.util.List;

/**
 * Class which allows an event creator to manually remove a user from the list of invited entrants for a particular event
 */
public class CancelEntrant {
    // Basically just remove the user from the InvitedList, US 02.06.04
    // Takes in the Entrant and Event objects
    public void removeCancelledEntrant(Entrant entrant, Event event) {

        // Sets the variables of the invitedList and entrantID
        String entrantID;
        List<String> invitedList = event.getPendingList();
        entrantID = entrant.getAndroid_id();

        // If it finds that the person is in the invitedList, remove them
        for (int i = 0; i < invitedList.size(); i++) {
            if (invitedList.get(i).equals(entrantID)) {
                event.getPendingList().remove(entrantID);
            }
        }

        // Updates the firebase, at least I hope it does Im trusting you Sasha
        EventDAL eventDAL = new EventDAL();
        eventDAL.updateEvent(event);

    }
}
