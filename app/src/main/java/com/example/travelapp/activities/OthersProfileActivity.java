package com.example.travelapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.Fragment.MyAdapter;
import com.example.travelapp.Fragment.ViewTripFragment;
import com.example.travelapp.R;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.AllChatsData;
import com.example.travelapp.models.Trip;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class OthersProfileActivity extends AppCompatActivity {

    private final static String TAG = "OthersProfileActivity Activity";

    TextView firstNameView;
    TextView lastNameView;
    TextView emailView;
    TextView stateInfoView;
    TextView phoneNumView;
    Button messageUser;

    ImageView profileImageView;
    String userId;
//    String uploaderId;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    List<String> tripId;
    List<Trip> trips;

    MyAdapter adapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().findItem(R.id.nav_search).setChecked(true);

        firstNameView = findViewById(R.id.user_header_first_name);
        lastNameView = findViewById(R.id.user_header_last_name);
        emailView = findViewById(R.id.user_header_user_email);
        stateInfoView = findViewById(R.id.user_header_user_state_info);
        phoneNumView = findViewById(R.id.user_header_user_phone_number);

        profileImageView = findViewById(R.id.user_header_profile_image);

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference(Constants.DATABASE_PATH_UPLOADS);


        Intent intent = getIntent();
        userId = intent.getStringExtra("USER_ID");

        tripId = new ArrayList<>();
        trips = new ArrayList<>();

        messageUser = findViewById(R.id.init_chat);

        messageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatNotification();

                Intent intent = new Intent(OthersProfileActivity.this, ChatActivity.class);
                intent.putExtra("userUid", userId);
                startActivity(intent);

            }
        });

        setUserInfoHeader();
        setUserTrips();

        recyclerView = findViewById(R.id.recyclerview_others);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MyAdapter(this, trips, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Trip trip = adapter.getItem(position);

                Bundle args = new Bundle();
                args.putString(ViewTripFragment.ARGUMENT_TRIPID, trip.getTrip_id());
                ViewTripFragment fragment = new ViewTripFragment();
                fragment.setArguments(args);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_others, fragment, "Trip_Info");
                fragmentTransaction.addToBackStack("Trip_Info");
                fragmentTransaction.commit();
            }
        });

        recyclerView.setAdapter(adapter);

    }

    private void chatNotification() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentUser.getUid();

        DatabaseReference reference = firebaseDatabase.getReference(Constants.DATABASE_PATH_Notifications);

        AllChatsData notificationData = new AllChatsData(currentUserId, userId);
        reference.child(userId).setValue(notificationData);


    }

    private void setUserInfoHeader() {

        Query query = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_USERS).orderByChild("uid").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String image = "" + ds.child("profilePictureUrl").getValue();
                    Log.d(TAG, "jiuminga" + image);
                    String firstname = "" + ds.child("firstName").getValue();
                    String lastname = "" + ds.child("lastName").getValue();
                    String email = "" + ds.child("email").getValue();
                    String stateinfo = "State Info: " + ds.child("stateInfo").getValue();
                    String phonenum = "Phone Num: " + ds.child("phoneNum").getValue();

                    firstNameView.setText(firstname);
                    lastNameView.setText(lastname);
                    emailView.setText(email);
                    stateInfoView.setText(stateinfo);
                    phoneNumView.setText(phonenum);

                    if (!image.isEmpty()) {
                        Picasso.get().load(image).into(profileImageView);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setUserTrips() {

        Query query = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_TRAVELHISTORY)
                .orderByKey()
                .equalTo(userId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.hasChildren()) {
                        DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                        for (DataSnapshot snapshot : singleSnapshot.getChildren()) {
                            String id = snapshot.child(Constants.DATABASE_FIELD_TRIPID).getValue().toString();
                            tripId.add(id);
                        }
                    }

                    if (tripId.size() > 0) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        for (int i = 0; i < tripId.size(); i++) {
                            Query query = reference.child(Constants.DATABASE_PATH_TRIPS)
                                    .orderByKey()
                                    .equalTo(tripId.get(i));

                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()) {
                                        DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                                        Trip trip = singleSnapshot.getValue(Trip.class);
                                        trips.add(trip);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }


                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    Intent intent = new Intent(OthersProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_search:
                    Intent intent2 = new Intent(OthersProfileActivity.this, SearchActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_travel_history:
                    Intent intent3 = new Intent(OthersProfileActivity.this, TravelHistoryActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.nav_AllChats:
                    Intent intent4 = new Intent(OthersProfileActivity.this, AllChatsActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.nav_profile:
                    Intent intent5 = new Intent(OthersProfileActivity.this, ProfileActivity.class);
                    startActivity(intent5);
                    break;

            }
            return false;
        }
    };

}
