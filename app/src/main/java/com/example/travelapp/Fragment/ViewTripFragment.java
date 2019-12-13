package com.example.travelapp.Fragment;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travelapp.R;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.Trip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewTripFragment extends Fragment {

    ImageView mImage;
    TextView mTitle;
    TextView mCity;
    TextView mState;
    TextView mDate;
    TextView mDays;
    TextView mDescription;
    DatabaseReference mDatabaseReference;

    private String mTripId;
    private Trip mTrip;

    private static final String TAG = "ViewTripFragment";
    public static final String ARGUMENT_TRIPID = "PostID";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTripId = (String) getArguments().get(ARGUMENT_TRIPID);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_TRIPS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);
        mImage = view.findViewById(R.id.trip_image);
        mTitle = view.findViewById(R.id.trip_title);
        mCity = view.findViewById(R.id.trip_city);
        mState = view.findViewById(R.id.trip_state);
        mDate = view.findViewById(R.id.trip_date);
        mDays = view.findViewById(R.id.trip_number_of_days);
        mDescription = view.findViewById(R.id.trip_description);
        getTripInfo();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void getTripInfo() {
        Query query = mDatabaseReference.orderByKey().equalTo(mTripId);
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
                        mCity.setText(mTrip.getCity());
                        mState.setText(Constants.MAP_NAMES[mTrip.getState()]);
                        if (mTrip.getDate().length() > 0) {
                            mDate.setText(mTrip.getDate());
                        } else {
                            mDate.setVisibility(View.GONE);
                        }
                        mDays.setText(String.valueOf(mTrip.getDays()));
                        if (mTrip.getDescription().length() > 0) {
                            mDescription.setText(mTrip.getDescription());
                        } else {
                            mDescription.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
