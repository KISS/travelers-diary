package com.example.travelapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.Fragment.AddTripFragment;
import com.example.travelapp.Fragment.ViewTripFragment;
import com.example.travelapp.R;
import com.example.travelapp.adapters.TravelFeedAdapter;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.Trip;
import com.example.travelapp.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TravelHistoryActivity extends AppCompatActivity implements AddTripFragment.AddTripFragmentHandler {

    private RecyclerView recyclerView;

    private TravelFeedAdapter adapter;

    private DatabaseReference mDatabase;

    private ProgressDialog progressDialog;


    FloatingActionButton mAddTripButton;

    //progress dialog

    //list to hold all the uploaded images
    private List<Trip> trips;
    //list to hold all users corresponding to trips
    private List<User> users;

    FirebaseUser fUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_history);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().findItem(R.id.nav_travel_history).setChecked(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        trips = new ArrayList<>();
        users = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        configureRecyclerView();

        getAllItem();
    }

    private void configureRecyclerView() {
        //creating adapter
        adapter = new TravelFeedAdapter(getApplicationContext(), trips, users, v -> {
            // Listener for click event on User Info.
            int position = (int) v.getTag();
            Trip trip = adapter.getItem(adapter.getItemCount() - 1 - position);

            Intent intent = new Intent(TravelHistoryActivity.this, OthersProfileActivity.class);
            intent.putExtra("USER_ID", trip.getUser_id());
            intent.putExtra("PARENT_PAGE", 1);
            startActivity(intent);
        }, v -> {
            // Listener for click event on Trip.
            int position = (int) v.getTag();
            Trip trip = adapter.getItem(adapter.getItemCount() - 1 - position);

            Bundle args = new Bundle();
            args.putString(ViewTripFragment.ARGUMENT_TRIPID, trip.getTrip_id());
            ViewTripFragment fragment = new ViewTripFragment();
            fragment.setArguments(args);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "Trip_Info");
            fragmentTransaction.addToBackStack("Trip_Info");
            fragmentTransaction.commit();
        });

        //adding adapter to
        recyclerView.setAdapter(adapter);
    }

    private void getAllItem() {

        progressDialog = new ProgressDialog(this);


        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        Query query = mDatabase.child(Constants.DATABASE_PATH_UPLOADS);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                trips.clear();
                users.clear();

                //dismissing the progress dialog
                progressDialog.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Trip upload = postSnapshot.getValue(Trip.class);

                    if (!upload.getUser_id().equals(fUser.getUid()) && upload.isIs_public()) {
                        trips.add(upload);
                        users.add(null);
                        getUserInfo(upload.getUser_id(), users.size() - 1);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error");
                progressDialog.dismiss();
            }
        });

    }

    private void getUserInfo(String userId, int index) {
        Query query = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_USERS).orderByKey().equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                    if (singleSnapshot != null) {
                        User user = singleSnapshot.getValue(User.class);
                        users.set(index, user);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // The function called when the Add A Trip link is clicked
    public void addATrip(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container, new AddTripFragment()).addToBackStack("Add a trip").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    Intent intent = new Intent(TravelHistoryActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_search:
                    Intent intent2 = new Intent(TravelHistoryActivity.this, SearchActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_travel_history:
//                    Intent intent3 = new Intent(TravelHistoryActivity.this, TravelHistoryActivity.class);
//                    startActivity(intent3);
                    break;
                case R.id.nav_AllChats:
                    Intent intent4 = new Intent(TravelHistoryActivity.this, AllChatsActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.nav_profile:
                    Intent intent5 = new Intent(TravelHistoryActivity.this, ProfileActivity.class);
                    startActivity(intent5);
                    break;

            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        // When there is a fragment attached, pop it and stay at the current activity, otherwise go to previous activity
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
