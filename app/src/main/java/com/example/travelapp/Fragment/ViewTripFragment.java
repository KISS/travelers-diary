package com.example.travelapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.travelapp.R;
import com.example.travelapp.activities.ChatActivity;
import com.example.travelapp.activities.OthersProfileActivity;
import com.example.travelapp.activities.TravelHistoryActivity;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.Trip;
import com.example.travelapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewTripFragment extends Fragment {

    private Toolbar mToolbar;
    private ImageView mImage;
    private TextView mTitle;
    private TextView mLocation;
    private TextView mDate;
    private TextView mDays;
    private TextView mDescription;
    private TextView mTextDays;
    private Button mEdit, mTravelFeedButton;
    private RelativeLayout mUserInfo;
    private ImageView mUserPhoto;
    private TextView mUserName;
    private DatabaseReference mDatabaseReference;

    private String mTripId;
    private Trip mTrip;

    private static final String TAG = "ViewTripFragment";
    public static final String ARGUMENT_TRIPID = "TripID";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTripId = (String) getArguments().get(ARGUMENT_TRIPID);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);
        mToolbar = view.findViewById(R.id.toolbar);
        mImage = view.findViewById(R.id.trip_image);
        mTitle = view.findViewById(R.id.trip_title);
        mLocation = view.findViewById(R.id.trip_location);
        mDate = view.findViewById(R.id.trip_date);
        mDays = view.findViewById(R.id.trip_number_of_days);
        mDescription = view.findViewById(R.id.trip_description);
        mTextDays = view.findViewById(R.id.text_days);
        mEdit = view.findViewById(R.id.edit_trip_button);
        mTravelFeedButton = view.findViewById(R.id.travel_feed_button);
        mUserInfo = view.findViewById(R.id.user_info);
        mUserPhoto = view.findViewById(R.id.user_photo);
        mUserName = view.findViewById(R.id.user_name);
        getTripInfo();
        addButtonClickListener();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void getTripInfo() {
        Query query = mDatabaseReference.child(Constants.DATABASE_PATH_TRIPS).orderByKey().equalTo(mTripId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                    if (singleSnapshot != null) {
                        mTrip = singleSnapshot.getValue(Trip.class);
                        Picasso.get().load(mTrip.getImage()).into(mImage);
                        if (mTrip.getTitle().length() > 0) {
                            mTitle.setText(mTrip.getTitle());
                        } else {
                            mTitle.setVisibility(View.GONE);
                        }
                        String location = mTrip.getCity().isEmpty() ? Constants.MAP_NAMES[mTrip.getState()] : mTrip.getCity() + ", " + Constants.MAP_NAMES[mTrip.getState()];
                        mLocation.setText(location);
                        if (mTrip.getDate().length() > 0) {
                            if (mTrip.getDays() == 1) {
                                mDate.setText(mTrip.getDate());
                            } else {
                                String startDate= mTrip.getDate();
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                                Calendar calendar = Calendar.getInstance();
                                try {
                                    calendar.setTime(sdf.parse(startDate));
                                    calendar.add(Calendar.DAY_OF_MONTH, mTrip.getDays() - 1);
                                    String datePeriod = startDate + " - " + sdf.format(calendar.getTime());
                                    mDate.setText(datePeriod);
                                } catch (ParseException e) {
                                    mDate.setText("");
                                    e.printStackTrace();
                                }
                            }
                        }
                        mDays.setText(String.valueOf(mTrip.getDays()));
                        if (mTrip.getDays() <= 1) {
                            mTextDays.setText(R.string.text_day);
                        }
                        if (mTrip.getDescription().length() > 0) {
                            mDescription.setText(mTrip.getDescription());
                        } else {
                            mDescription.setVisibility(View.GONE);
                        }
                        // Different displays for my trip and other users' trip
                        if (mTrip.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            mEdit.setVisibility(View.VISIBLE);
                        } else {
                            getUserInfo(mTrip.getUser_id());
                            mUserInfo.setVisibility(View.VISIBLE);
                            mTravelFeedButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUserInfo(String userId) {
        Query query = mDatabaseReference.child(Constants.DATABASE_PATH_USERS).orderByKey().equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                    if (singleSnapshot != null) {
                        User user = singleSnapshot.getValue(User.class);
                        mUserName.setText(user.getFirstName() + " " + user.getLastName());
                        if (!user.getProfilePictureUrl().isEmpty()) {
                            Picasso.get().load(user.getProfilePictureUrl()).into(mUserPhoto);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addButtonClickListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(ARGUMENT_TRIPID, mTripId);
                AddTripFragment fragment = new AddTripFragment();
                fragment.setArguments(args);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "Edit_Trip");
                fragmentTransaction.addToBackStack("Edit_Trip");
                fragmentTransaction.commit();
            }
        });

        mTravelFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TravelHistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
