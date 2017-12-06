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

import java.util.Calendar;
import java.util.Date;

import io.github.huang_chenyu.thosedays.events.DateChangedEvent;


public class MenuFragment extends Fragment {

    private Button pickDateButton;
    private Button publishButton;
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
        publishButton = rootView.findViewById(R.id.publish_button);
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


        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
