package io.github.huang_chenyu.thosedays;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.huang_chenyu.thosedays.events.DateChangedEvent;
import io.github.huang_chenyu.thosedays.events.StartDetailActivityEvent;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private HumanActivityListAdapter humanActivityListAdapter;
    private Database2 db;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab;
    private FloatingActionButton calendarFab;
    private FloatingActionButton refreshFab;
    private Date lastDate;
    private DatePickerDialog datePickerDialog;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;



    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity, container, false);

        recyclerView = rootView.findViewById(R.id.activity_list);

        fab = rootView.findViewById(R.id.fab);
        calendarFab = rootView.findViewById(R.id.calendar_fab);
        refreshFab = rootView.findViewById(R.id.refresh_fab);

        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_backward);

        setupFabListeners();

        //this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // Below is the right format for DB queries.
        getAndRenderListOfActivities();

        return rootView;
    }


    View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.fab:
                    animateFAB();
                    break;
                case R.id.calendar_fab:
                    Calendar calendar = Calendar.getInstance();
                    String time = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE);
                    lastDate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
//        EventBus.getDefault().post(new DateChangedEvent(new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))));

                    datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            String newTime = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
//                pickDateButton.setText(newTime);
                            lastDate = new Date(year, monthOfYear, dayOfMonth);
                            EventBus.getDefault().postSticky(new DateChangedEvent(lastDate));
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                    datePickerDialog.show();

                    Log.d("Raj", "calendar fab");
                    break;
                case R.id.refresh_fab:
                    EventBus.getDefault().postSticky(new DateChangedEvent(lastDate));
                    Log.d("Raj", "refresh fab");
                    break;
            }
        }
    };

    private void animateFAB(){

        if(isFabOpen){
            fab.startAnimation(rotate_backward);
            calendarFab.startAnimation(fab_close);
            refreshFab.startAnimation(fab_close);
            calendarFab.setClickable(false);
            refreshFab.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            calendarFab.startAnimation(fab_open);
            refreshFab.startAnimation(fab_open);
            calendarFab.setClickable(true);
            refreshFab.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");
        }
    }

    private void setupFabListeners() {
        fab.setOnClickListener(fabListener);
        calendarFab.setOnClickListener(fabListener);
        refreshFab.setOnClickListener(fabListener);
//        Calendar calendar = Calendar.getInstance();
//
//        String time = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE);
//        lastDate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
////        EventBus.getDefault().post(new DateChangedEvent(new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))));
//
//        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
//                String newTime = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
////                pickDateButton.setText(newTime);
//                lastDate = new Date(year, monthOfYear, dayOfMonth);
//                EventBus.getDefault().postSticky(new DateChangedEvent(lastDate));
//            }
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                datePickerDialog.show();
//            }
//        });
    }

    private void getAndRenderListOfActivities() {
        //todo change date to a Date object, and set it to today
        String date = "2017-12-2";
        List<HumanActivity> activities = db.queryByDate(date);

        humanActivityListAdapter = new HumanActivityListAdapter(getContext(), activities);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(humanActivityListAdapter);

        humanActivityListAdapter.setOnItemClickListener(new HumanActivityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                HumanActivity activity = humanActivityListAdapter.getQueue().get(position);
                EventBus.getDefault().post(new StartDetailActivityEvent(activity));
            }
        });
    }

    private void getAndRenderListOfActivities(String date) {
        List<HumanActivity> activities = db.queryByDate(date);

        humanActivityListAdapter = new HumanActivityListAdapter(getContext(), activities);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(humanActivityListAdapter);

        humanActivityListAdapter.setOnItemClickListener(new HumanActivityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                HumanActivity activity = humanActivityListAdapter.getQueue().get(position);
                EventBus.getDefault().post(new StartDetailActivityEvent(activity));
            }
        });
    }

    @Subscribe(sticky = true)
    public void onDateChangedSticky(DateChangedEvent event){
        EventBus.getDefault().removeStickyEvent(event);

        String month = String.format("%02d", event.date.getMonth()+1);
        String date = month +"/" +event.date.getDate()+ "/" + event.date.getYear();

        Log.d("MICKIE", date);
        getAndRenderListOfActivities(date);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setDb(Database2 db) {
        this.db = db;
    }
}
