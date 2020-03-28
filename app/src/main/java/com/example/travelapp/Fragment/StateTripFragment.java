package com.example.travelapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.activities.ProfileActivity;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StateTripFragment extends DialogFragment {

    public interface TripItemClickHandler {
        void onTripClicked(String id);
    }

    TripItemClickHandler mClickHandler;

    private TextView mTitle;
    private RecyclerView mRecyclerView;
    List<String> mTripIds;
    List<Trip> mTrips;
    MyAdapter mAdapter;
    int mState;

    public static boolean showing = false;

    private final String TAG = "State Trip Fragment";
    public static final String ARGUMENT_STATE = "State";

    public StateTripFragment() {
        // Used to avoid displaying the same DialogFragment multiple times.
        showing = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mClickHandler = (TripItemClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("The activity that this fragment is attached must be a TripItemClickHandler");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_trips, container);
        mTitle = view.findViewById(R.id.title);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mTripIds = new ArrayList<>();
        mTrips = new ArrayList<>();
        mState = (int) getArguments().get(ARGUMENT_STATE);
        mTitle.setText(Constants.MAP_NAMES[mState]);
        getDialog().setTitle("hahahah");
        configureRecyclerView();
        return view;
    }

    private void configureRecyclerView() {
        mAdapter = new MyAdapter(getContext(), mTrips, false, v -> {
            int position = (int) v.getTag();
            Trip trip = mAdapter.getItem(position);
            mClickHandler.onTripClicked(trip.getTrip_id());
            this.dismiss();
        });
        mRecyclerView.setAdapter(mAdapter);
        getTripIdsAndTrips();
    }

    private void getTripIdsAndTrips() {
        mTripIds.clear();
        mTrips.clear();

        Query query = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_TRAVELHISTORY)
                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

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
                            Log.d(TAG, "onDataChange: found a post id: " + id);
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
                Log.d(TAG, "getPosts: getting post information for: " + mTripIds.get(i));
                Query query = reference.child(Constants.DATABASE_PATH_TRIPS)
                        .orderByKey()
                        .equalTo(mTripIds.get(i));

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                            Trip trip = singleSnapshot.getValue(Trip.class);
                            if (trip.getState() == mState) {
                                mTrips.add(trip);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        } else {
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        showing = false;
        super.onCancel(dialog);
    }

    @Override
    public void dismiss() {
        showing = false;
        super.dismiss();
    }
}
