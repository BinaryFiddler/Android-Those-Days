package io.github.huang_chenyu.thosedays;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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

    ImageView myImage;


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
        myImage = (ImageView) rootView.findViewById(R.id.test_image);

        saveButton = rootView.findViewById(R.id.save);
        cancelButton = rootView.findViewById(R.id.cancel);


        setActivityOverview();

        setButtonListeners();

        renderImages();
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
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.running));
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

    private void renderImages(){
        List<String> photos = new LinkedList<>(activity.getPhotoPaths());

        File imgFile = new  File(photos.get(0));

        Log.d("photos path", photos.get(0));
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            myImage.setImageBitmap(myBitmap);

        }
    }
}
