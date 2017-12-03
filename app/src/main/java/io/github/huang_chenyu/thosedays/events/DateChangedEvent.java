package io.github.huang_chenyu.thosedays.events;

import java.util.Date;

/**
 * Created by Chenyu on 02/12/2017.
 */

public class DateChangedEvent {
    public Date date;

    public DateChangedEvent(Date date){
        this.date = date;
    }
}
