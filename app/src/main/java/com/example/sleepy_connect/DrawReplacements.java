package com.example.sleepy_connect;

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
        int eventCapacity = event.eventCapacity;

        Random random = new Random();

        // How many new entrants do we need?
        int slotsToFill = eventCapacity - pendingList.size();

        // Safety check
        if (slotsToFill <= 0) {
            Log.i("DrawReplacements", "Pending list is already full.");
            return;
        }

        // Loop and fill pendingList until full
        for (int i = 0; i < slotsToFill; i++) {

            // If waiting list is empty, stop early
            if (waitingList.isEmpty()) {
                Log.e("DrawReplacements", "No more users in waiting list.");
                break;
            }

            // Pick a random person from waiting list
            String randomEntrant = waitingList.get(random.nextInt(waitingList.size()));

            // Add them to pending list
            pendingList.add(randomEntrant);

            // Remove them from waitingList so they aren't picked again
            waitingList.remove(randomEntrant);
        }

        // Push updates to Firestore
        EventDAL eventDAL = new EventDAL();
        eventDAL.updateEvent(event);
    }
}

