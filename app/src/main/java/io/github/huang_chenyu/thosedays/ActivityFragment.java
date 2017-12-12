package io.github.huang_chenyu.thosedays;


import android.app.DatePickerDialog;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.huang_chenyu.thosedays.events.DateChangedEvent;
import io.github.huang_chenyu.thosedays.events.ProcessingCompleteEvent;
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
    private TextView dateTextView;
    private TextView noDataTextView;
    private Date lastDate;
    private DatePickerDialog datePickerDialog;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private ImageView backdrop;



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
        dateTextView = rootView.findViewById(R.id.date_text_view);
        noDataTextView = rootView.findViewById(R.id.no_data_textview);
        backdrop = rootView.findViewById(R.id.main_backdrop);

        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.rotate_backward);

        setupFabListeners();

        //this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE);
        lastDate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        dateTextView.setText(time);
        setMonthDrawable(new Date());

        // Below is the right format for DB queries.
//        getAndRenderListOfActivities();

        return rootView;
    }

    private void setupFabListeners() {
        fab.setOnClickListener(fabListener);
        calendarFab.setOnClickListener(fabListener);
        refreshFab.setOnClickListener(fabListener);
    }

    private final View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.fab:
                    animateFAB();
                    break;
                case R.id.calendar_fab:
                    Calendar calendar = Calendar.getInstance();
                    datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
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

    private void getAndRenderListOfActivities(String date) {
        List<HumanActivity> activities = db.queryByDate(date);

        if (activities == null || activities.isEmpty()){
            noDataTextView.setVisibility(View.VISIBLE);
        }else{
            noDataTextView.setVisibility(View.GONE);
        }

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

        setMonthDrawable(event.date);
        String month = String.format("%02d", event.date.getMonth()+1);
        String date = month +"/" +event.date.getDate()+ "/" + event.date.getYear();
        dateTextView.setText(date);

        getAndRenderListOfActivities(date);
    }

    @Subscribe(sticky = true)
    public void onEvent(ProcessingCompleteEvent event){
        EventBus.getDefault().removeStickyEvent(event);
        EventBus.getDefault().postSticky(new DateChangedEvent(lastDate));
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

    private void setMonthDrawable(Date date){
        int month = date.getMonth();
        int img = 0;
        switch (month){
            case 0:
                img = R.drawable.january;
                break;
            case 1:
                img = R.drawable.february;
                break;
            case 2:
                img = R.drawable.march;
                break;
            case 3:
                img = R.drawable.april;
                break;
            case 4:
                img = R.drawable.may;
                break;
            case 5:
                img = R.drawable.june;
                break;
            case 6:
                img = R.drawable.july;
                break;
            case 7:
                img = R.drawable.august;
                break;
            case 8:
                img = R.drawable.september;
                break;
            case 9:
                img = R.drawable.october;
                break;
            case 10:
                img = R.drawable.november;
                break;
            case 11:
                img = R.drawable.december;
                break;
        }
        Picasso.with(getContext())
                .load(img)
                .fit()
                .into(backdrop);
    }

}
