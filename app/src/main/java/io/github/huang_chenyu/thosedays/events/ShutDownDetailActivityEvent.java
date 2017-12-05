package io.github.huang_chenyu.thosedays.events;

import io.github.huang_chenyu.thosedays.HumanActivity;

/**
 * Created by Chenyu on 11/12/17.
 */

public class ShutDownDetailActivityEvent {
    public HumanActivity humanActivity;

    public ShutDownDetailActivityEvent(HumanActivity humanActivity){
        this.humanActivity = humanActivity;
    }

    public ShutDownDetailActivityEvent(){
        humanActivity = null;
    }
}
