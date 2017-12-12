package io.github.huang_chenyu.thosedays;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.Date;
import io.github.huang_chenyu.thosedays.events.DateChangedEvent;

import io.github.huang_chenyu.thosedays.events.ProcessingCompleteEvent;
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXT_STORAGE);
            }
        }else{
            new RunAlgoTask().execute();
        }
        db = new Database2();
        db.openDB(this);
        if (activityFragment == null){
            activityFragment = new ActivityFragment();
            activityFragment.setDb(db);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content, activityFragment).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXT_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new RunAlgoTask().execute();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.content, new HumanActivityDetailFragment(event.activity)).commit();
    }

    @Subscribe
    public void onEvent(ShutDownDetailActivityEvent event){
        Log.d("chenyu", "cancel?");
        if (event.humanActivity.getComments() != null){
            db.updateComment(event.humanActivity);
        }
        if (activityFragment == null){
            activityFragment = new ActivityFragment();
            activityFragment.setDb(db);
        }

//        mm/dd/yyyy
        Date date = new Date();
        int month = Integer.parseInt(event.humanActivity.getDate().substring(0, 2));
        int day = Integer.parseInt(event.humanActivity.getDate().substring(3, 5));
        int year = Integer.parseInt(event.humanActivity.getDate().substring(6));
        date.setYear(year);
        date.setMonth(month - 1);
        date.setDate(day);
        EventBus.getDefault().postSticky(new DateChangedEvent(date));

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.content, activityFragment).commit();
    }

    @Override
    public void onBackPressed() {
    }

    private class RunAlgoTask extends AsyncTask<Void, Integer, Void> {
        protected void onPreExecute(){
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Void doInBackground(Void... urls) {
            Algorithm.process(getApplicationContext());
            return null;
        }

        protected void onPostExecute(Void result) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            EventBus.getDefault().postSticky(new ProcessingCompleteEvent());
            Toast.makeText(getApplicationContext(), "Processing complete!", Toast.LENGTH_SHORT).show();
        }
    }
}
