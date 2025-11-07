package com.example.sleepy_connect;

import java.util.ArrayList;

public class CommunityCentre {
    public String communityCentreName;                      // Required
    public String communityCentreLocation;                  // Required
    public ArrayList<String> events;                        // IDs of events hosted at this community centre

    public CommunityCentre(String name, String location) {
        this.communityCentreName = name;
        this.communityCentreLocation = location;
        this.events = new ArrayList<>();
    }

    public void addEvent(String eventID) {
        this.events.add(eventID);
    }

    public String getCommunityCentreName() {
        return communityCentreName;
    }
}
