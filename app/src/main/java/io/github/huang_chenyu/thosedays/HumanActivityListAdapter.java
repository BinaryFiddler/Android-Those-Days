package io.github.huang_chenyu.thosedays;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chenyu on 11/5/17.
 */

public class HumanActivityListAdapter extends RecyclerView.Adapter<HumanActivityListAdapter.ViewHolder> {

    private List<HumanActivity> queue;
    private Context adapterContext;


    /***** Creating OnItemClickListener *****/

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Pass in the contact array into the constructor
    public HumanActivityListAdapter(Context context, List<HumanActivity> humanActivities) {
        queue = humanActivities;
        adapterContext = context;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView activityName;
        public TextView activityTime;
        public TextView activityLocation;
        public ImageView activityImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            activityName = (TextView) itemView.findViewById(R.id.activity_name);
            activityImage = (ImageView) itemView.findViewById(R.id.activity_image);
            activityTime = (TextView) itemView.findViewById(R.id.activity_time);
            activityLocation = (TextView) itemView.findViewById(R.id.activity_location);
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View humanActivityView = inflater.inflate(R.layout.human_activity_item, parent, false);

        // Return a new holder instance
        final ViewHolder viewHolder = new ViewHolder(humanActivityView);
        humanActivityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        HumanActivity humanActivity = queue.get(position);
        // Set item views based on your views and data model
        TextView textView = viewHolder.activityName;
        textView.setText(humanActivity.getActivityName());

        TextView activityTimeView = viewHolder.activityTime;
        activityTimeView.setText(humanActivity.getStartTime() + "-" + humanActivity.getEndTime());

        TextView activityLocationView = viewHolder.activityLocation;
        String location = "Latitude: " + humanActivity.getLat() + "\n" + "Longtitude" + humanActivity.getLon();
        activityLocationView.setText(location);

        ImageView imageView = viewHolder.activityImage;
        imageView.setImageDrawable(adapterContext.getResources().getDrawable(R.drawable.images));
    }

    @Override
    public int getItemCount() {
        return queue.size();
    }

    public List<HumanActivity> getQueue() {
        return queue;
    }
}
