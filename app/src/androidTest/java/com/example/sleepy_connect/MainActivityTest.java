package com.example.sleepy_connect;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

// using spies here to half mock objects
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    // Checking if dals are made
    @Test
    public void testDal() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                assertNotNull(activity.entrantDal);
                assertNotNull(activity.eventDal);
                assertNotNull(activity.communityCentreDAL);
            });
        }
    }

    // Checking the start button
    @Test
    public void testStartPress() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {

                // capture startActivity()
                MainActivity spyActivity = spy(activity);
                spyActivity.user = new Entrant("user1");
                spyActivity.androidID = "user1";

                doNothing().when(spyActivity).startActivity(any(Intent.class));

                spyActivity.startPress(new View(spyActivity));

                verify(spyActivity, times(1)).startActivity(any(Intent.class));
            });
        }
    }

    // Checking if we load user before loading main activity
    @Test
    public void testStartPressUserNull() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {

                MainActivity spyActivity = spy(activity);
                spyActivity.user = null;

                spyActivity.startPress(new View(spyActivity));

                verify(spyActivity, never()).startActivity(any(Intent.class));
            });
        }
    }
}