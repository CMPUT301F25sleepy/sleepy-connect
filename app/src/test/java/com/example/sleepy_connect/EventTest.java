package com.example.sleepy_connect;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;

public class EventTest {

    private Event event;
    private CommunityCentre centre;

    @Before
    public void setup() {
        centre = new CommunityCentre("Test Centre", "Edmonton AB");

        long now = System.currentTimeMillis();

        event = new Event("0", "Soccer Session", centre, "creator123", now - 50000, 
                now + 50000, now, now + 86400000, "4:00 PM", 30, true);
    }
    
    // Testing the constructor
    @Test
    public void testConstructorDates() {
        long now = System.currentTimeMillis();
        assertTrue(event.getCreatedDate() <= now);

        String expectedDay = Instant.ofEpochMilli(event.getEventStartDate())
                .atZone(ZoneId.systemDefault())
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        assertEquals(expectedDay, event.getEventDayOfWeek());
    }
    
    // Testing list changes
    @Test
    public void testAddToWaitlist() {
        event.addToWaitlist("user1");
        assertTrue(event.getWaitingList().contains("user1"));
    }

    @Test
    public void testRemoveFromWaitlist() {
        event.addToWaitlist("user1");
        event.removeFromWaitlist("user1");
        assertFalse(event.getWaitingList().contains("user1"));
    }


    // Testing remove from all events
    @Test
    public void testRemoveFromEventLists() {
        event.addToWaitlist("user1");
        event.addToAcceptedList("user1");
        event.addToDeclinelist("user1");
        event.getPendingList().add("user1");

        Event updated = Event.removeFromEventLists(event, "user1");

        assertFalse(updated.getWaitingList().contains("user1"));
        assertFalse(updated.getAcceptedList().contains("user1"));
        assertFalse(updated.getDeclinedList().contains("user1"));
        assertFalse(updated.getPendingList().contains("user1"));
    }

    // Testing waitlist size
    @Test
    public void testGetWaitlistSize() {
        event.addToWaitlist("user1");
        event.addToWaitlist("user1");
        event.addToWaitlist("user1");

        assertEquals(3, event.getWaitlistSize());
    }
}