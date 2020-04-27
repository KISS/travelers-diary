package com.example.travelapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.activities.StateTripActivity;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentPageHistory extends Fragment {

    public interface TripItemClickHandler {
        void onTripClicked(String id);
    }

    FragmentPageHistory.TripItemClickHandler mClickHandler;

    private TextView mNoHistoryText;
    private RecyclerView mRecyclerView;

    List<String> mTripIds;
    List<Trip> mTrips;
    MyAdapter mAdapter;
    int mState;

    private final String TAG = "History Page Fragment";
    public static final String ARGUMENT_STATE_NO = "State No";
    public static final String ARGUMENT_STATE_STATUS = "State Status";

    public FragmentPageHistory() {
        // Required empty public constructor
    }

    public static FragmentPageHistory newInstance(int state, int status) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_STATE_NO, state);
        args.putInt(ARGUMENT_STATE_STATUS, status);
        FragmentPageHistory fragment = new FragmentPageHistory();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mClickHandler = (FragmentPageHistory.TripItemClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("The activity that this fragment is attached must be a TripItemClickHandler");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_history, container, false);
        mNoHistoryText = view.findViewById(R.id.no_history_text);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mTripIds = new ArrayList<>();
        mTrips = new ArrayList<>();
        mState = (int) getArguments().get(ARGUMENT_STATE_NO);
        init();
        return view;
    }

    private void init() {
        if (getArguments().getInt(ARGUMENT_STATE_STATUS) == 0) {
            mNoHistoryText.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(new MyAdapter(getContext(), mTrips, false, null));
        } else {
            configureRecyclerView();
        }
    }

    private void configureRecyclerView() {
        mAdapter = new MyAdapter(getContext(), mTrips, false, v -> {
            StateTripActivity.showing = false;
            int position = (int) v.getTag();
            Trip trip = mAdapter.getItem(position);
            mClickHandler.onTripClicked(trip.getTrip_id());
        });
        mRecyclerView.setAdapter(mAdapter);
        getTripIdsAndTrips();
    }

    private void getTripIdsAndTrips() {
        mTripIds.clear();
        mTrips.clear();

        Query query = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_TRIPS_OF_STATES)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByKey()
                .equalTo(String.valueOf(mState));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTripIds.clear();
                mTrips.clear();
                if (dataSnapshot != null) {
                    if (dataSnapshot.hasChildren()) {
                        DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                        for (DataSnapshot snapshot : singleSnapshot.getChildren()) {
                            String id = snapshot.child(Constants.DATABASE_FIELD_TRIPID).getValue().toString();
//                            Log.d(TAG, "onDataChange: found a post id: " + id);
                            mTripIds.add(id);
                        }
                    }
                    getTripsInTripIDList();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getTripsInTripIDList() {
        if (mTripIds.size() > 0) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            for (int i = 0; i < mTripIds.size(); i++) {
//                Log.d(TAG, "getPosts: getting post information for: " + mTripIds.get(i));
                Query query = reference.child(Constants.DATABASE_PATH_TRIPS)
                        .orderByKey()
                        .equalTo(mTripIds.get(i));

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                            Trip trip = singleSnapshot.getValue(Trip.class);
//                            Log.d(TAG, "onDataChange: found a post: " + trip.getTitle());
                            int index = 0;
                            // Consider special case when mTripIds.size() < mTrips.size(), eg. trip removed
                            for (; index < Math.min(mTrips.size(), mTripIds.size()); index++) {
                                // When edit a trip, find the trip and update info
                                if (trip.getTrip_id().equals(mTripIds.get(index))) {
                                    mTrips.set(index, trip);
                                    mAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                            // Consider special case when index < mTrips.size()
                            if (index == mTrips.size()) {
                                mTrips.add(trip);
                                mAdapter.notifyDataSetChanged();
                                if (mNoHistoryText.getVisibility() == View.VISIBLE) {
                                    mNoHistoryText.setVisibility(View.INVISIBLE);
                                }
                                if (mRecyclerView.getVisibility() == View.GONE) {
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        } else {
            mAdapter.notifyDataSetChanged(); //still need to notify the adapter if the list is empty
            mNoHistoryText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }
}
