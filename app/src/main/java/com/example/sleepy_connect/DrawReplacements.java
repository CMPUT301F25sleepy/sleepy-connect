package com.example.sleepy_connect;

import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Random;

/**
 * Class which draws new entrants from the waitlist when an invited entrant declines their invitation or the event creator cancels their invitation
 */
public class DrawReplacements {

    public void drawReplacementApp(Event event) {

        List<String> waitingList = event.getWaitingList();
        List<String> pendingList = event.getPendingList();
        List<String> enrolledList = event.getAcceptedList();
        int eventCapacity = event.eventCapacity;

        Random random = new Random();

        // Changed logic to capacity - (enrolled + pending)
        int slotsToFill = eventCapacity - (enrolledList.size() + pendingList.size());

        if (slotsToFill <= 0) {
            Log.i("DrawReplacements", "Event already has enough enrolled + invited participants.");
            return;
        }

        for (int i = 0; i < slotsToFill; i++) {

            if (waitingList.isEmpty()) {
                Log.e("DrawReplacements", "No more users in waiting list.");
                break;
            }

            String randomEntrant = waitingList.get(random.nextInt(waitingList.size()));

            pendingList.add(randomEntrant);
            waitingList.remove(randomEntrant);

            sendSelectedNotification(randomEntrant, event);
        }

        // Push updates to Firestore
        EventDAL eventDAL = new EventDAL();
        eventDAL.updateEvent(event);
    }

    /**
     * Creates a Notification object and sends it to the selected entrant.
     */
    private void sendSelectedNotification(String userID, Event event) {

        Notification notif = new Notification(
                event.getEventName(),   // event name
                true,               // selected = true
                false,              // cancelled = false
                event.getEventID()  // event ID
        );

        // Save to entrant’s notification list in Firestore
        notif.sendNotification(userID);
    }
}

