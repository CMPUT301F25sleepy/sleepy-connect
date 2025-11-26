package com.example.sleepy_connect;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class EntrantTest {

    // Test creation
    @Test
    public void testEntrantCreation() {
        Entrant entrant = new Entrant("67");

        assertEquals("67", entrant.getAndroid_id());
        assertNull(entrant.getFirst_name());
        assertNull(entrant.getLast_name());
        assertNull(entrant.getBirthday());
        assertNull(entrant.getEmail());
        assertNull(entrant.getPhone_number());
        assertNull(entrant.getUsername());
    }

    // Test adding events
    @Test
    public void testAddCreatedEvent() {
        Entrant entrant = new Entrant("67");

        entrant.addCreatedEvent("1");

        assertTrue(entrant.getCreated_event_list().contains("1"));
    }

    // Test list operations
    @Test
    public void testListOperations() {
        Entrant entrant = new Entrant("67");

        entrant.addToAllEventList("event1");
        entrant.addToAllEventList("event2");

        assertEquals(2, entrant.getAll_event_list().size());
        assertTrue(entrant.getAll_event_list().contains("event1"));

        entrant.removeFromAllEventList("event1");

        assertFalse(entrant.getAll_event_list().contains("event1"));
        assertEquals(1, entrant.getAll_event_list().size());
    }

    // Test the notification list
    @Test
    public void testNotificationList() {
        Entrant entrant = new Entrant("67");

        assertNotNull("Notification list should not be null", entrant.getNotification_list());

        entrant.setNotification_list(new ArrayList<>());
        assertNotNull(entrant.getNotification_list());
    }
}