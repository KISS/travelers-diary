package com.example.travelapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    private final String TAG = "Search Activity";

    private EditText searchField;
    private ImageButton searchButton;
    private RecyclerView resultList;
    private DatabaseReference UsersDatabaseReference;
    private FirebaseRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().findItem(R.id.nav_search).setChecked(true);

        UsersDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        searchField = (EditText) findViewById(R.id.search_box);

        searchButton = (ImageButton) findViewById(R.id.search_btn);

        resultList = (RecyclerView) findViewById(R.id.result_list);
        layoutManager = new LinearLayoutManager(this);
        resultList.setLayoutManager(layoutManager);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = searchField.getText().toString();

                if (!searchText.equals(""))
                    firebaseSearch(searchText);

            }
        });

    }

    private void firebaseSearch(String searchText) {


        Log.d(TAG, "Search started.");

        // SELECT * FROM Users WHERE firstName = "searchText%"
        Query query = UsersDatabaseReference.orderByChild("firstName").startAt(searchText).endAt("searchText\uf8ff");

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, new SnapshotParser<User>() {
                    @NonNull
                    @Override
                    public User parseSnapshot(@NonNull DataSnapshot snapshot) {
                        User user = new User(snapshot.child("email").getValue().toString(),
                                snapshot.child("firstName").getValue().toString(),
                                snapshot.child("lastName").getValue().toString(),
                                snapshot.child("profilePictureUrl").getValue().toString(),
                                Long.parseLong(snapshot.child("statesInfo").getValue().toString()),
                                snapshot.child("uid").getValue().toString());
                        return user;
                    }
                }).build();

        adapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(options) {

            @Override
            public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_users, parent, false);

                return new UsersViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(UsersViewHolder holder, final int position, User user) {
                holder.setDetails(SearchActivity.this,user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getProfilePictureUrl());
            }

        };

        resultList.setAdapter(adapter);
        adapter.startListening();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_search:
                    Intent intent2 = new Intent(SearchActivity.this, SearchActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_travel_history:
                    Intent intent3 = new Intent(SearchActivity.this, TravelHistoryActivity.class);
                    startActivity(intent3);
                    break;
//                            case R.id.nav_wishlist:
//                                Intent intent4 = new Intent(SearchActivity.this, MainActivity.class);
//                                startActivity(intent4);
//                                break;
                case R.id.nav_profile:
                    Intent intent5 = new Intent(SearchActivity.this, ProfileActivity.class);
                    startActivity(intent5);
                    break;

            }
            return false;
        }
    };


    // viewholder

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        View view;

        public UsersViewHolder(View itemView) {
            super(itemView);

            view = itemView;

        }

        public void setDetails(Context ctx, String userFirstName, String userLastName ,String userEmail, String userImage) {

            TextView user_name = (TextView) view.findViewById(R.id.name_text);
            TextView user_email = (TextView) view.findViewById(R.id.email_text);
            ImageView user_image = (ImageView) view.findViewById(R.id.profile_image);


            user_name.setText(userFirstName + " " +userLastName);
            user_email.setText(userEmail);

            if (!userImage.isEmpty()){
                Picasso.get().load(userImage).into(user_image);
            }



        }



    }



}
