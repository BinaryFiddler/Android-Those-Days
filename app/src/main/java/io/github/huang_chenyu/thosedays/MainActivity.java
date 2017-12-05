package io.github.huang_chenyu.thosedays;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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
        db.closeDB();
    }

    @Subscribe
    public void onEvent(StartDetailActivityEvent event){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new HumanActivityDetailFragment(event.activity)).commit();
    }

    @Subscribe
    public void onEvent(ShutDownDetailActivityEvent event){
        if (event.humanActivity != null){
            db.updateComment(event.humanActivity);
        }
        if (activityFragment == null){
            activityFragment = new ActivityFragment();
            activityFragment.setDb(db);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content, activityFragment).commit();
    }
}
