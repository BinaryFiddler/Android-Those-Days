package io.github.huang_chenyu.thosedays;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.github.huang_chenyu.thosedays.events.ShutDownDetailActivityEvent;

public class HumanActivityDetailFragment extends Fragment {

    HumanActivity activity;


    ImageView imageView;
    TextView activityName;
    TextView activityTime;
    TextView activityLocation;

    TagView tagsGroup;
    EditText comment;

    Button saveButton;
    Button cancelButton;
    Button tweetButton;
    LinearLayout imageBox;

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
        tagsGroup = (TagView)rootView.findViewById(R.id.tag_container);
//        tags = rootView.findViewById(R.id.tag_container);
        comment = rootView.findViewById(R.id.comment);

        imageBox = (LinearLayout) rootView.findViewById(R.id.image_box);
        saveButton = rootView.findViewById(R.id.save);
        cancelButton = rootView.findViewById(R.id.cancel);
        tweetButton = rootView.findViewById(R.id.tweet);

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

        tweetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // TODO add images when done
                TweetComposer.Builder builder = new TweetComposer.Builder(HumanActivityDetailFragment.this.getContext())
                        .text(activity.getActivityName().substring(0,1).toUpperCase() + activity.getActivityName().substring(1) + " at " + activity.getLocation() + "!");
                builder.show();
            }
        });
    }

    private void setActivityOverview() {
        setActivityIcon();
        activityName.setText(activity.getActivityName());
        activityTime.setText(activity.getDuration());

        activityLocation.setText("Location: " + activity.getLocation());

        comment.setText(activity.getComments());

        ArrayList<Tag> tags = new ArrayList<>();
        for (String t:activity.getTags()){
            if (t == null || t.equals("")){
                continue;
            }
            Tag tag = new Tag(t);
            tag.layoutColor = getResources().getColor(R.color.colorAccent);
            tag.isDeletable = false;
            tag.tagTextSize = 15;
            tag.radius = 15;
            tags.add(tag);
        }
        tagsGroup.addTags(tags);

        renderImages();
    }

    private void renderImages(){
        List<String> photos = new LinkedList<>(activity.getPhotoPaths());

        final float scale = getResources().getDisplayMetrics().density;
        int imgWidth  = (int) (80 * scale);
        int imgHeight = (int) (80 * scale);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgWidth, imgHeight);


        for (final String path: photos){
            final File imgFile = new  File(path);
            if(imgFile.exists()){

                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(layoutParams);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogLayout = inflater.inflate(R.layout.image_dialog, null);
                        ImageView largeImage  = (ImageView) dialogLayout.findViewById(R.id.large_image);
                        Picasso.with(getContext())
                                .load(imgFile)
                                .into(largeImage);
                        dialog.setView(dialogLayout);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        dialog.show();
                    }
                });

                imageBox.addView(imageView);

                Picasso.with(getContext())
                        .load(imgFile)
                        .into(imageView);
            }
        }
    }

    private void setActivityIcon(){
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
    }
}
