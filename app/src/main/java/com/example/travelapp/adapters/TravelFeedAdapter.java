package com.example.travelapp.adapters;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.Trip;
import com.example.travelapp.models.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class TravelFeedAdapter extends RecyclerView.Adapter<TravelFeedAdapter.ViewHolder> {

    private Context context;
    private List<Trip> tripsUploads;
    private List<User> usersUploads;
    private View.OnClickListener clickOnUser;
    private View.OnClickListener clickOnTrip;

    public TravelFeedAdapter(Context context, List<Trip> tripsUploads, List<User> usersUploads,
                             View.OnClickListener clickOnUser, View.OnClickListener clickOnTrip) {
        this.tripsUploads = tripsUploads;
        this.usersUploads = usersUploads;
        this.context = context;
        this.clickOnUser = clickOnUser;
        this.clickOnTrip = clickOnTrip;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_feed_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trip upload = tripsUploads.get(tripsUploads.size() -  1 - position);
        final User upload_user = usersUploads.get(tripsUploads.size() -  1 - position);

        if (upload_user != null) {
            holder.userName.setText(upload_user.getFirstName() + " " + upload_user.getLastName());
            Picasso.get().load(upload_user.getProfilePictureUrl()).resize(dpToPx(50), dpToPx(50)).centerCrop().into(holder.userPhoto);
        }
//        holder.textViewTitle.setText(upload.getTitle());
//        holder.textViewCity.setText(upload.getCity());
//        holder.textViewDate.setText("Date: "+upload.getDate());
//        holder.textViewDays.setText("Trip For "+upload.getDays()+" Days");
//
//        holder.textViewDiscription.setText(upload.getDescription());

        holder.location.setText(upload.getCity() + ", " + Constants.MAP_NAMES[upload.getState()]);
        Picasso.get().load(upload.getImage()).resize(getScreenWidthInPx(), dpToPx(270)).centerCrop().into(holder.tripImageView);
        holder.userInfo.setTag(position);
        holder.tripImageView.setTag(position);
        holder.userInfo.setOnClickListener(clickOnUser);
        holder.tripImageView.setOnClickListener(clickOnTrip);
    }

    private int getScreenWidthInPx() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public Trip getItem(int position) {
        return tripsUploads.get(position);
    }

    @Override
    public int getItemCount() {
        return tripsUploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        public TextView textViewTitle;
//        public TextView textViewCity;
//        public TextView textViewState;
//        public TextView textViewDate;
//        public TextView textViewDays;
//        public TextView textViewDiscription;

        public RelativeLayout userInfo;
        public TextView userName;
        public TextView location;
        public ImageView userPhoto;
        public ImageView tripImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            userInfo = itemView.findViewById(R.id.user_info);
            userName = itemView.findViewById(R.id.user_name);
            location = itemView.findViewById(R.id.location);
            userPhoto = itemView.findViewById(R.id.user_photo);
            tripImageView = (ImageView) itemView.findViewById(R.id.tripImage);
//            textViewTitle = (TextView) itemView.findViewById(R.id.titleTv);
//            textViewCity = (TextView) itemView.findViewById(R.id.cityTv);
//            textViewDate = (TextView) itemView.findViewById(R.id.dateTv);
//            textViewDays = (TextView) itemView.findViewById(R.id.daysTv);
//            textViewDiscription = (TextView) itemView.findViewById(R.id.disTv);
        }
    }

    private int dpToPx(float dpValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics()));
    }
}
