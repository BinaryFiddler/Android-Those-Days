package io.github.huang_chenyu.thosedays;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

import io.github.huang_chenyu.thosedays.events.DateChangedEvent;
import io.github.huang_chenyu.thosedays.events.ShutDownDetailActivityEvent;
import io.github.huang_chenyu.thosedays.events.StartDetailActivityEvent;

public class MainActivity extends AppCompatActivity {

    final static int READ_EXT_STORAGE = 1;
    private ActivityFragment activityFragment;
    private Database2 db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXT_STORAGE);
            }
        }
        db = new Database2();
        db.openDB(this);
        Algorithm.process(getApplicationContext());
        if (activityFragment == null){
            activityFragment = new ActivityFragment();
            activityFragment.setDb(db);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.menu, new MenuFragment()).replace(R.id.content, activityFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
//        db.closeDB();
    }

    @Subscribe
    public void onEvent(StartDetailActivityEvent event){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new HumanActivityDetailFragment(event.activity)).commit();
    }

    @Subscribe
    public void onEvent(ShutDownDetailActivityEvent event){
        Log.d("chenyu", "cancel?");
        if (event.humanActivity.getComments() != null){
            db.updateComment(event.humanActivity);
        }
//        if (activityFragment == null){
            activityFragment = new ActivityFragment();
            activityFragment.setDb(db);
//        }

        Date date = new Date();
        int month = Integer.parseInt(event.humanActivity.getDate().substring(0, 2));
        int day = Integer.parseInt(event.humanActivity.getDate().substring(3, 5));
        int year = Integer.parseInt(event.humanActivity.getDate().substring(6));
        date.setYear(year);
        date.setMonth(month - 1);
        date.setDate(day);
        EventBus.getDefault().postSticky(new DateChangedEvent(date));

        getSupportFragmentManager().beginTransaction().replace(R.id.content, activityFragment).commit();
//        mm/dd/yyyy

    }
}
