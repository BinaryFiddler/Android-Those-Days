package io.github.huang_chenyu.thosedays;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import io.github.huang_chenyu.thosedays.events.ShutDownDetailActivityEvent;

public class HumanActivityDetailFragment extends Fragment {

    HumanActivity activity;


    ImageView imageView;
    TextView activityName;
    TextView activityTime;
    TextView activityLocation;

    LinearLayout tags;
    EditText comment;


    Button saveButton;
    Button cancelButton;


    public HumanActivityDetailFragment(){

    }

    @SuppressLint("ValidFragment")
    public HumanActivityDetailFragment(HumanActivity activity) {
        this.activity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_human_activity_detail, container, false);

        imageView = rootView.findViewById(R.id.activity_image);
        activityName = rootView.findViewById(R.id.activity_name);
        activityTime = rootView.findViewById(R.id.activity_time);
        activityLocation = rootView.findViewById(R.id.activity_location);
        tags = rootView.findViewById(R.id.tag_container);
        comment = rootView.findViewById(R.id.comment);

        saveButton = rootView.findViewById(R.id.save);
        cancelButton = rootView.findViewById(R.id.cancel);


        setActivityOverview();

        setButtonListeners();


        return rootView;
    }

    private void setButtonListeners() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ShutDownDetailActivityEvent());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ShutDownDetailActivityEvent());
            }
        });

    }

    private void setActivityOverview() {
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.running));
        activityName.setText(activity.getActivityName());
        activityTime.setText(activity.getDuration());

        activityLocation.setText(activity.getLocation());

        for (String t:activity.getTags()){
            TextView textView = new TextView(getContext());
            textView.setText(t);
            tags.addView(textView);
        }
    }
}