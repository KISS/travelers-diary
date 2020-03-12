package com.example.travelapp.Fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.Trip;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<Trip> mList;
    boolean mLongPressEnabled;
    public OnItemClickListener mListener;
    View.OnClickListener mListenerSingleClick;

    public MyAdapter(Context context, List<Trip> list, boolean longPressEnabled, View.OnClickListener listener) {
        mContext = context;
        mList = list;
        mLongPressEnabled = longPressEnabled;
        mListenerSingleClick = listener;
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
        holder.mDate.setText(trip.getDate());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mListenerSingleClick);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {

        ImageView mImage;
        TextView mTitle;
        TextView mCity;
        TextView mState;
        TextView mDate;

        public MyAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.trip_image);
            mTitle = itemView.findViewById(R.id.trip_title);
            mCity = itemView.findViewById(R.id.trip_city);
            mState = itemView.findViewById(R.id.trip_state);
            mDate = itemView.findViewById(R.id.trip_date);

            if (mLongPressEnabled) {
                itemView.setOnClickListener(this);
                itemView.setOnCreateContextMenuListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.select_action);
            Boolean status = mList.get((int)v.getTag()).isIs_public();
            MenuItem editPrivacy = menu.add(Menu.NONE, 1, 1, status ? R.string.set_as_private : R.string.set_as_public);
            MenuItem editTrip = menu.add(Menu.NONE, 2, 2, R.string.edit_trip);
            MenuItem editDelete = menu.add(Menu.NONE, 3, 3, R.string.remove_trip);

            editPrivacy.setOnMenuItemClickListener(this);
            editTrip.setOnMenuItemClickListener(this);
            editDelete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onPrivacyLongPress(position);
                            return true;
                        case 2:
                            mListener.onEditTripLongPress(position);
                            return true;
                        case 3:
                            mListener.onRemoveTripLongPress(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public Trip getItem(int position) {
        return mList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onPrivacyLongPress(int position);

        void onEditTripLongPress(int position);

        void onRemoveTripLongPress(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}