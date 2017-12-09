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
                String newComment = comment.getText().toString();
                activity.setComments(newComment);
                EventBus.getDefault().post(new ShutDownDetailActivityEvent(activity));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ShutDownDetailActivityEvent(activity));
            }
        });

    }

    private void setActivityOverview() {
        setActivityIcon();
        activityName.setText(activity.getActivityName());
        activityTime.setText(activity.getDuration());

        activityLocation.setText(activity.getLocation());
        comment.setText(activity.getComments());

        for (String t:activity.getTags()){
            Button button = new Button(getContext());
            button.setText(t);
            button.setAllCaps(false);
            button.setClickable(false);
//            button.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
            tags.addView(button);
        }
    }

    private void setActivityIcon() {
        String name = activity.getActivityName();

        if (name.equals("Lying down")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.lying_down));
        }
        else if (name.equals("Sitting")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.sitting));
        }
        else if (name.equals("Walking")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.walking));
        }
        else if (name.equals("Running")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.running));
        }
        else if (name.equals("Bicycling")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.bicycling));
        }
        else if (name.equals("Sleeping")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.sleeping));
        }
        else if (name.equals("Lab work")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.lab_work));
        }
        else if (name.equals("Exercise")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.exercise));
        }
        else if (name.equals("Cooking")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.cooking));
        }
        else if (name.equals("Shopping")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.shopping));
        }
        else if (name.equals("Strolling")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.strolling));
        }
        else if (name.equals("Drinking (alcohol)")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.drinking));
        }
        else if (name.equals("Bathing - shower")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.bathing));
        }
        else if (name.equals("Cleaning")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.cleaning));
        }
        else if (name.equals("Doing laundry")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.doing_laundry));
        }
        else if (name.equals("Washing dishes")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.washing_dishes));
        }
        else if (name.equals("Watching tv")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.watching_tv));
        }
        else if (name.equals("Surfing the internet")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.surfing_internet));
        }
        else if (name.equals("Singing")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.singing));
        }
        else if (name.equals("Talking")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.talking));
        }
        else if (name.equals("Computer work")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.computer_work));
        }
        else if (name.equals("Eating")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.eating));
        }
        else if (name.equals("Toilet")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.toilet));
        }
        else if (name.equals("Grooming")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.grooming));
        }
        else if (name.equals("Dressing")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.dressing));
        }
        else if (name.equals("Standing")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.standing));
        }
        else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.toilet));
        }
    }
}
