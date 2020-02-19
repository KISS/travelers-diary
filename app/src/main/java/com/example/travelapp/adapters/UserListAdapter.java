package com.example.travelapp.adapters;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.activities.OthersProfileActivity;
import com.example.travelapp.activities.SearchActivity;
import com.example.travelapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private static final String TAG = "UserListAdapter";

    private ArrayList<User> users;
    private Context context;
    private View.OnClickListener listener;

    public UserListAdapter(Context context, ArrayList<User> users, View.OnClickListener listener) {
        this.users = users;
        this.context = context;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setDetails(Context ctx, String userFirstName, String userLastName ,String userEmail, String userImage) {

            TextView user_name = (TextView) view.findViewById(R.id.name_text);
            TextView user_email = (TextView) view.findViewById(R.id.email_text);
            ImageView user_image = (ImageView) view.findViewById(R.id.profile_image);

            user_name.setText(userFirstName + " " +userLastName);
            user_email.setText(userEmail);
            Log.d(TAG, "setDetails: " + userImage);

            if (!userImage.isEmpty()){
                Picasso.get().load(userImage).into(user_image);
            }

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_users, viewGroup, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.setDetails(context, users.get(i).getFirstName(),
                users.get(i).getLastName(),
                users.get(i).getEmail(),
                users.get(i).getProfilePictureUrl());

        viewHolder.itemView.setTag(i);
        viewHolder.itemView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public User getItem(int position) {
        return users.get(position);
    }

}
