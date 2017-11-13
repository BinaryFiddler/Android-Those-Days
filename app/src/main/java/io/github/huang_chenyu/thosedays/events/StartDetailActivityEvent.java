package io.github.huang_chenyu.thosedays.events;

import io.github.huang_chenyu.thosedays.HumanActivity;

/**
 * Created by Chenyu on 11/12/17.
 */

public class StartDetailActivityEvent {

    public HumanActivity activity;

    public StartDetailActivityEvent(HumanActivity activity){
        this.activity = activity;
    }
}
