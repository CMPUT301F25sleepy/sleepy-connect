package com.example.sleepy_connect;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommunityCentreTest {

    private CommunityCentre centre;

    @Before
    public void setUp() {
        centre = new CommunityCentre("Westside Centre", "Edmonton");
    }

    // Testing making obj
    @Test
    public void testConstructor() {
        assertEquals("Westside Centre", centre.getCommunityCentreName());
        assertEquals("Edmonton", centre.getCommunityCentreLocation());
        assertNotNull(centre.events);
        assertTrue(centre.events.isEmpty());
    }

    // Testing adding events
    @Test
    public void testAddEvent() {
        centre.addEvent("event1");
        centre.addEvent("event2");

        assertEquals(2, centre.events.size());
        assertTrue(centre.events.contains("event1"));
        assertTrue(centre.events.contains("event2"));
    }
}