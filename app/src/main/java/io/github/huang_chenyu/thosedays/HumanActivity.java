package io.github.huang_chenyu.thosedays;

import java.util.ArrayList;

/**
 * Created by chenyu on 11/5/17.
 */

public class HumanActivity {
    private String activityName;
    private String duration;

    public HumanActivity(){
        activityName = "running";
        duration = "10:03 - 13:12";
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

    public String getDuration() {
        return duration;
    }
}
