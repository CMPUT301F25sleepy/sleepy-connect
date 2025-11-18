package com.example.sleepy_connect;

import android.util.Log;

import java.util.List;
import java.util.Random;

/**
 * Class which draws new entrants from the waitlist when an invited entrant declines their invitation or the event creator cancels their invitation
 */
public class DrawReplacements {
    public void drawReplacementApp(Event event) {
        // Should take in the waiting list and when called, pick a person at random from the waiting list
        // Add this random person to the invited list, making sure to update the event using eventDAL
        // Also maybe call the notification function to notify them here? Thats a feature tho not a requirement
        // For the "if cancels or rejected" maybe check if the wait list is at max capacity, if not then draw a replacement
        // If its at max then state that the list is full

        List<String> waitingList = event.getWaitingList();
        List<String> pendingList = event.getPendingList();
        int waitlistCapacity = event.waitlistCapacity;

        // Gets a random entrant from the waitingList
        Random random = new Random();
        String randomEntrant = waitingList.get(random.nextInt(waitingList.size()));

        // If the size of the waiting list is currently under capacity, add the user
        if (waitingList.size() < waitlistCapacity) {
            pendingList.add(randomEntrant);
            // Could implement a notification call here in the future
        }
        // Else throw an error if no space
        else {
            Log.e("Adding Entrant To Waitlist", "No more space left in the waitlist");
        }

        // Updates the firebase, at least I hope it does Im trusting you Sasha
        EventDAL eventDAL = new EventDAL();
        eventDAL.updateEvent(event);
    }
}
