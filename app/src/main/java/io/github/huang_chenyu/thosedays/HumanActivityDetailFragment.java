package io.github.huang_chenyu.thosedays;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.squareup.picasso.Picasso;

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
        tags = rootView.findViewById(R.id.tag_container);
        comment = rootView.findViewById(R.id.comment);

        imageBox = (LinearLayout) rootView.findViewById(R.id.image_box);
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
}
