package com.example.sleepy_connect;

import org.junit.Test;
import static org.junit.Assert.*;


public class NotificationTest {

    // Testing base constructor
    @Test
    public void testConstructor() {
        Notification n = new Notification("EventA", true);

        assertEquals("EventA", n.getEvent_name());
        assertTrue(n.isSelected());
        assertFalse(n.isCancelled());
        assertFalse(n.read);
    }

    // Testing optinal fields
    @Test
    public void testConstructorExtra() {
        Notification n = new Notification("EventB", false, true, "1");

        assertEquals("EventB", n.getEvent_name());
        assertFalse(n.isSelected());
        assertTrue(n.isCancelled());
        assertEquals("1", n.getEventID());
        assertFalse(n.read);
    }

    // Testing setters
    @Test
    public void testSetters() {
        Notification n = new Notification();
        n.setEvent_name("EventC");
        n.setSelected(true);
        n.setEventID("1");

        assertEquals("EventC", n.getEvent_name());
        assertTrue(n.isSelected());
        assertEquals("1", n.getEventID());
    }
}