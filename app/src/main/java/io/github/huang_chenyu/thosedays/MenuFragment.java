package io.github.huang_chenyu.thosedays;


import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Date;

import io.github.huang_chenyu.thosedays.events.DateChangedEvent;
import io.github.huang_chenyu.thosedays.events.ShutDownDetailActivityEvent;
import io.github.huang_chenyu.thosedays.events.StartDetailActivityEvent;


public class MenuFragment extends Fragment {

    private Button pickDateButton;
    DatePickerDialog datePickerDialog;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        pickDateButton = rootView.findViewById(R.id.pick_date_button);
        setUpButtonListeners();

        return rootView;
    }

    private void setUpButtonListeners() {
        Calendar calendar = Calendar.getInstance();

        String time = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE);
        pickDateButton.setText(time);
//        EventBus.getDefault().post(new DateChangedEvent(new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))));

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                String newTime = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                pickDateButton.setText(newTime);
                EventBus.getDefault().postSticky(new DateChangedEvent(new Date(year, monthOfYear, dayOfMonth)));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    @Subscribe
    public void onEvent(StartDetailActivityEvent event){
        pickDateButton.setClickable(false);
    }

    @Subscribe
    public void onEvent(ShutDownDetailActivityEvent event){
        pickDateButton.setClickable(true);
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
}
