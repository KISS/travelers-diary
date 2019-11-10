package com.example.travelapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.travelapp.Fragment.AddTripFragment;
import com.example.travelapp.R;

public class TravelHistoryActivity extends AppCompatActivity implements AddTripFragment.AddTripFragmentHandler {

    FloatingActionButton mAddTripButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_history);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().findItem(R.id.nav_travel_history).setChecked(true);

        mAddTripButton = findViewById(R.id.add_trip_button);

        mAddTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.fragment_container, new AddTripFragment()).addToBackStack("Add a trip").commit();
            }
        });

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
                    Intent intent2 = new Intent(TravelHistoryActivity.this, MainActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_travel_history:
                    Intent intent3 = new Intent(TravelHistoryActivity.this, TravelHistoryActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.nav_wishlist:
                    Intent intent4 = new Intent(TravelHistoryActivity.this, MainActivity.class);
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
