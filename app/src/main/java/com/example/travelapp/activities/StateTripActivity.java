package com.example.travelapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.travelapp.Fragment.AddTripFragment;
import com.example.travelapp.Fragment.FragmentPageExplore;
import com.example.travelapp.Fragment.FragmentPageHistory;
import com.example.travelapp.Fragment.ViewTripFragment;
import com.example.travelapp.R;
import com.example.travelapp.adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class StateTripActivity extends AppCompatActivity implements AddTripFragment.AddTripFragmentHandler,
        FragmentPageHistory.TripItemClickHandler {

    private Toolbar mToolbar;
    private TextView mStateText;
    private ViewPager mViewPager;
    private TabLayout mTabs;
    private ViewPagerAdapter mAdapter;

    private boolean hasChange = false;
    public static boolean showing = false;
    private static final String TAG = "State Trip Activity";
    public static final String EXTRA_STATE_NO = "State No";
    public static final String EXTRA_STATE_NAME = "State Name";
    public static final String EXTRA_STATE_VALUE = "State Value";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_trip);
        mToolbar = findViewById(R.id.toolbar);
        mStateText = findViewById(R.id.state_text);
        mViewPager = findViewById(R.id.pager);
        mTabs = findViewById(R.id.tab_layout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        init();
    }

    private void init() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showing = false;
                Intent intent = new Intent();
                intent.putExtra("HasChange", hasChange);
                setResult(0, intent);
                finish();
            }
        });
        mStateText.setText(getIntent().getStringExtra(EXTRA_STATE_NAME));
        setUpAdapter();
    }

    private void setUpAdapter() {
        // Create the adapter.
        int state = getIntent().getIntExtra(EXTRA_STATE_NO, -1);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(FragmentPageHistory.newInstance(
                state, getIntent().getIntExtra(EXTRA_STATE_VALUE, 0)),
                getResources().getString(R.string.history));
        mAdapter.addFragment(FragmentPageExplore.newInstance(state),getResources().getString(R.string.explore));

        // Add the adapter to the viewpager.
        mViewPager.setAdapter(mAdapter);

        // Link the tab layout to the viewpager.
        mTabs.setupWithViewPager(mViewPager);
    }

    // Called when any trip in a FragmentPageExplore is clicked
    @Override
    public void onTripClicked(String id) {
        Bundle args = new Bundle();
        args.putString(ViewTripFragment.ARGUMENT_TRIPID, id);
        ViewTripFragment fragment = new ViewTripFragment();
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "Trip_Info");
        fragmentTransaction.addToBackStack("Trip_Info");
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = menuItem -> {

        showing = false;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(StateTripActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_search:
                Intent intent2 = new Intent(StateTripActivity.this, SearchActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_travel_history:
                Intent intent3 = new Intent(StateTripActivity.this, TravelHistoryActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_AllChats:
                Intent intent4 = new Intent(StateTripActivity.this, AllChatsActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_profile:
                Intent intent5 = new Intent(StateTripActivity.this, ProfileActivity.class);
                startActivity(intent5);
                break;

        }
        return false;
    };

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // get which view was focused
            View v = getCurrentFocus();

            if (HideKeyboard.getInstance().isTouchOutsideView(v, ev)) {
                HideKeyboard.getInstance().hideSoftInput(v.getWindowToken(), this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        showing = false;
        Intent intent = new Intent();
        intent.putExtra("HasChange", hasChange);
        setResult(0, intent);
        super.onBackPressed();
    }

    @Override
    public void notifyChange() {
        hasChange = true;
    }
}
