package com.example.travelapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.models.Trip;
import com.squareup.picasso.Picasso;

import java.util.List;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class TravelFeedAdapter extends RecyclerView.Adapter<TravelFeedAdapter.ViewHolder> {

    private Context context;
    private List<Trip> tripsUploads;

    public TravelFeedAdapter(Context context, List<Trip> tripsUploads) {
        this.tripsUploads = tripsUploads;
        this.context = context;
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
        final Trip upload = tripsUploads.get(position);


        holder.textViewTitle.setText(upload.getTitle());
        holder.textViewCity.setText(upload.getCity());
        holder.textViewDate.setText("Date: "+upload.getDate());
        holder.textViewDays.setText("Trip For "+upload.getDays()+" Days");

        holder.textViewDiscription.setText(upload.getDescription());

        Picasso.get().load(upload.getImage()).into(holder.tripImageView);


    }

    @Override
    public int getItemCount() {
        return tripsUploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTitle;
        public TextView textViewCity;
        public TextView textViewState;
        public TextView textViewDate;
        public TextView textViewDays;
        public TextView textViewDiscription;

        public ImageView tripImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            tripImageView = (ImageView) itemView.findViewById(R.id.tripImage);
            textViewTitle = (TextView) itemView.findViewById(R.id.titleTv);
            textViewCity = (TextView) itemView.findViewById(R.id.cityTv);
            textViewDate = (TextView) itemView.findViewById(R.id.dateTv);
            textViewDays = (TextView) itemView.findViewById(R.id.daysTv);
            textViewDiscription = (TextView) itemView.findViewById(R.id.disTv);
        }
    }
}
