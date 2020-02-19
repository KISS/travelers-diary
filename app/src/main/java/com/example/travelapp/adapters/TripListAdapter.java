package com.example.travelapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.models.Trip;
import com.example.travelapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder> {

    private static final String TAG = "TripListAdapter";

    private ArrayList<Trip> trips;
    private Context context;
    private View.OnClickListener listener;

    public TripListAdapter(ArrayList<Trip> trips, Context context, View.OnClickListener listener) {
        this.trips = trips;
        this.context = context;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTripDetails(Context context, String city, String image) {

            TextView cityView = view.findViewById(R.id.search_trip_city);
            ImageView tripImageView = view.findViewById(R.id.search_trip_image);

            cityView.setText(city);
            Picasso.get().load(image).into(tripImageView);


        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_trips, parent, false);
        return new TripListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setTripDetails(context, trips.get(position).getCity(),
                trips.get(position).getImage());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public Trip getItem(int position) {
        return trips.get(position);
    }
}
