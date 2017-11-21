package io.github.huang_chenyu.thosedays;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chenyu on 11/5/17.
 */

public class HumanActivity {
    private String activityName;
    private String location;
    private List<String> tags;

    private String endTime;
    private String startTime;
    private String lat;
    private String lon;

    public HumanActivity(){
        activityName = "running";
        endTime = "10:03";
        startTime = "13:12";
        location = "La Jolla";
        tags = new LinkedList<>();
        tags.add("Running");
        tags.add("Fun");
        lat = "32.881154";
        lon = "-117.235564";
    }
    public static ArrayList<HumanActivity> createActivitiesList(int numContacts) {
        ArrayList<HumanActivity> activities = new ArrayList<HumanActivity>();

        for (int i = 1; i <= numContacts; i++) {
            activities.add(new HumanActivity());
        }

        return activities;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getDuration() {return (startTime + " - " + endTime);}



    public List<String> getTags() {
        return tags;
    }

    public String getLocation() {

        //TODO remove location, return getPlace(lat, lon) via Google APIs
        return location;
    }

    public String getEndTime() {return endTime;}

    public String getStartTime() {return startTime;}

    public String getLat() {return lat;}

    public String getLon() {return lon;}
}
