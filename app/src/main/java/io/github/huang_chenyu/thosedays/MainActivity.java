package io.github.huang_chenyu.thosedays;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.github.huang_chenyu.thosedays.events.ShutDownDetailActivityEvent;
import io.github.huang_chenyu.thosedays.events.StartDetailActivityEvent;
import io.github.huang_chenyu.thosedays.Algorithm;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Algorithm.process(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu, new MenuFragment()).replace(R.id.content, new ActivityFragment()).commit();
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
    }

    @Subscribe
    public void onEvent(StartDetailActivityEvent event){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new HumanActivityDetailFragment(event.activity)).commit();
    }

    @Subscribe
    public void onEvent(ShutDownDetailActivityEvent event){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ActivityFragment()).commit();
    }
}
