package com.example.travelapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.activities.ChatActivity;
import com.example.travelapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserChatListAdapter extends RecyclerView.Adapter<UserChatListAdapter.MyHolder> {

    Context context;
    List<User> userList;

    public UserChatListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        final String userUid = userList.get(i).getUid();

        String userImage = userList.get(i).getProfilePictureUrl();
        String userName = userList.get(i).getFirstName();
        final String userEmail = userList.get(i).getEmail();

        myHolder.mUserListName.setText(userName);
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.ic_person_black_24dp).into(myHolder.mUserPic);

        } catch (Exception e) {

        }

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userUid", userUid);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView mUserPic;
        TextView mUserListName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mUserPic = itemView.findViewById(R.id.userPic);
            mUserListName = itemView.findViewById(R.id.userListName);

        }
    }
}
