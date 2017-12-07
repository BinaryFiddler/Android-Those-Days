package io.github.huang_chenyu.thosedays;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenyu on 11/5/17.
 */

public class HumanActivity {
    private String activityName;
    private String location;
    private Set<String> tags;
    private String date;
    private String endTime;
    private String startTime;
    private String lat;
    private String lon;
    private String comments;
    private Set<String> photoPaths;

    public HumanActivity(String activityName, Set<String> tags, String date, String endTime, String startTime, String lat, String lon) {
        this.activityName = activityName;
        this.tags = tags;
        this.date = date;
        this.endTime = endTime;
        this.startTime = startTime;
        this.lat = lat;
        this.lon = lon;
        this.location = null;
        this.comments = null;
        this.photoPaths = new HashSet<>();
    }

    public HumanActivity(String activityName, Set<String> tags, String date, String endTime,
                         String startTime, String lat, String lon, String loc, String comm, Set<String> photos ) {
        this.activityName = activityName;
        this.tags = tags;
        this.date = date;
        this.endTime = endTime;
        this.startTime = startTime;
        this.lat = lat;
        this.lon = lon;
        this.location = loc;
        this.comments = comm;
        this.photoPaths = photos;
    }

    public HumanActivity(){
        activityName = "running";
        date = "12/1/2017";
        endTime = "10:03";
        startTime = "13:12";
        location = "La Jolla";
        tags = new HashSet<>();
        tags.add("Running");
        tags.add("Fun");
        lat = "32.881154";
        lon = "-117.235564";
    }

    public static ArrayList<HumanActivity> createActivitiesList(int num) {
        ArrayList<HumanActivity> activities = new ArrayList<HumanActivity>();

        for (int i = 1; i <= num; i++) {
            activities.add(new HumanActivity());
        }

        return activities;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getDuration() {return (startTime + " - " + endTime);}



    public Set<String> getTags() {
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

    public String getDate() {return date;}

    public String getComments() {return comments;}

    public Set<String> getPhotoPaths() {return photoPaths;}

    public void setComments(String comments) {
        this.comments = comments;
    }
    public void printAll() {
        Log.d("HumanActivity", getDate());
        Log.d("HumanActivity", getActivityName() + ", " + getStartTime() + " - " + getEndTime());
        Log.d("HumanActivity", getLat() + ", " + getLon());

        for( String str : tags){
            Log.d("HumanActivity - Tags", str);
        }
    }

}
