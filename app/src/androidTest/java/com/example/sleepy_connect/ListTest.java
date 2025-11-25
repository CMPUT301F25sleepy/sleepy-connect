package com.example.sleepy_connect;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static
        androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class ListTest {
    // This is a test for manipulation of Waiting List, Pending List, Enrolled List, Declined List

    // Test I can do (mainly checking for database updates):
    // - checking notification interactions (Accept, Decline, Opt out) works
    // - Joining/Leaving the Waitlist
    // - (Accepting, Declining Invite) in Event Details

    public void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testEventDetailWaitList() {
        /// tests if joining and leaving the waitlist updates the database correctly

        // starts with pressing the start
        boolean start = false;
        while (!start) {
            onView(withId(R.id.start_button)).perform(click());
            try {
                onView(withText("Community"))
                        .check(matches(isDisplayed()));
                start = true;
            } catch (Throwable ignored) {

            }
        }

        onView(withText("Community")).check(matches(isDisplayed()));

        // throws a delay to wait for database to load all community centres
        delay();
        onData(anything()).inAdapterView(withId(R.id.list_view)).atPosition(0).perform(click());
        onView(withText("Events")).check(matches(isDisplayed()));
        delay();
        onData(anything()).inAdapterView(withId(R.id.event_list_view)).atPosition(0).perform(click());
        onView(withText("Event Details")).check(matches(isDisplayed()));

        onView(withId(R.id.waitlist_join_button)).check(matches(isDisplayed()));
        onView(withId(R.id.waitlist_join_button)).perform(click());

        delay();
        onView(withText("Return")).perform(click());

        // check if user is inside the waitlist
        Entrant currentUser = UserViewModel.getUser().getValue();
        Event selectedEvent = EventViewModel.getEvent().getValue();
        ArrayList<String> waiting_list = selectedEvent.getWaitingList();
        assertTrue("user should be in waiting list", waiting_list.contains(currentUser.getAndroid_id()));

        // tests leaving the waitlist and checking database
        onView(withId(R.id.leave_waitlist_button)).check(matches(isDisplayed()));
        onView(withId(R.id.leave_waitlist_button)).perform(click());

        selectedEvent = EventViewModel.getEvent().getValue();
        ArrayList<String> waiting_list2 = selectedEvent.getWaitingList();
        assertFalse("user should NOT be in waiting list", waiting_list2.contains(currentUser.getAndroid_id()));
    }
}