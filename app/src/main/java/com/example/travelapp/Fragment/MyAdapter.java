package com.example.travelapp.Fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.Trip;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context mContext;
    List<Trip> mList;
    int mSelectedPosition = -1;
    View.OnClickListener mListener;

    public MyAdapter(Context context, List<Trip> list, View.OnClickListener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.view_trip_abstract, null);
        return new MyAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int position = i;
        Trip trip = getItem(position);
        MyAdapterViewHolder holder = (MyAdapterViewHolder) viewHolder;
        Picasso.get().load(trip.getImage()).resize(100, 100).centerCrop().into(holder.mImage);
        holder.mTitle.setText(trip.getTitle());
        holder.mCity.setText(trip.getCity());
        holder.mState.setText(Constants.MAP_NAMES[trip.getState()]);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public String getSelectedPostId() {
        return mSelectedPosition == -1 ? "" : mList.get(mSelectedPosition).getTrip_id();
    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mTitle;
        TextView mCity;
        TextView mState;

        public MyAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.trip_image);
            mTitle = itemView.findViewById(R.id.trip_title);
            mCity = itemView.findViewById(R.id.trip_city);
            mState = itemView.findViewById(R.id.trip_state);
        }
    }

    public Trip getItem(int position) {
        return mList.get(position);
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }
}


