package com.example.travelapp.activities;


import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.adapters.UserListAdapter;
import com.example.travelapp.models.HitsObject;
import com.example.travelapp.models.User;
import com.example.travelapp.models.UserHitsList;
import com.example.travelapp.util.ElasticSearchAPI;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.PendingIntent.getActivity;

public class SearchActivity extends AppCompatActivity {

    private final static String TAG = "Search Activity";
    private final static String BASE_URL = "http://35.245.31.8//elasticsearch/users/Users/";

    private EditText searchField;
    private ImageButton searchButton;
    private RecyclerView userResultView;
    private DatabaseReference UsersDatabaseReference;
    //    private FirebaseRecyclerAdapter adapter;
    private UserListAdapter adapter;
    //    private RecyclerView.LayoutManager layoutManager;
    private String elasticSearchPassword;
    //    private User user;
    private ArrayList<User> users;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

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

        userResultView = (RecyclerView) findViewById(R.id.result_list);
        userResultView.setLayoutManager(new LinearLayoutManager(this));
        userResultView.setAdapter(adapter);
        getElasticSearchPassword();

//        searchField.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if(i == KeyEvent.KEYCODE_ENTER){
//                    InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if(imm.isActive()){
//                        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0 );
//                        search();
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    private void search() {
        String searchText = searchField.getText().toString();
        users = new ArrayList<User>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ElasticSearchAPI elasticSearchAPI = retrofit.create(ElasticSearchAPI.class);

        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", Credentials.basic("user", elasticSearchPassword));

        String searchString = "";

        if (!searchText.equals("")) {
//                    firebaseSearch(searchText);
//                    user = new User

            StringBuilder stringBuilder = new StringBuilder(searchText);

            // multithread maybe?
            for(int i = 0; i <= searchText.length() * 2; i += 2) {
                stringBuilder.insert(i, "*");
            }

            searchString = searchString + "firstName:" + stringBuilder.toString() + " lastName:" + stringBuilder.toString() + " email:" + stringBuilder.toString();
        }

        Call<HitsObject> call = elasticSearchAPI.search(headerMap, "OR", searchString);

        call.enqueue(new Callback<HitsObject>() {
            @Override
            public void onResponse(Call<HitsObject> call, Response<HitsObject> response) {

                UserHitsList userHitsList = new UserHitsList();
                String jsonResponse = "";

                try {
                    Log.d(TAG, "onResponse: server response: " + response.toString());

                    if (response.isSuccessful()) {
                        userHitsList = response.body().getUserHitsList();
                    } else {
                        jsonResponse = response.errorBody().string();
                    }

                    Log.d(TAG, "onResponse: hits: " + userHitsList);

                    for (int i = 0; i < userHitsList.getUserIndex().size(); i++) {
                        Log.d(TAG, "onResponse: data: " + userHitsList.getUserIndex().get(i).getUser().toString());
                        users.add(userHitsList.getUserIndex().get(i).getUser());
                    }

                    Log.d(TAG, "onResponse: size: " + users.size());
                    //setup the list of users
                    setUpUsersList();

                } catch (NullPointerException e) {
                    Log.e(TAG, "onResponse: NullPointerException: " + e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    Log.e(TAG, "onResponse: IndexOutOfBoundsException: " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "onResponse: IOException: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<HitsObject> call, Throwable t) {
                Log.e(TAG, "onFailure" + t.getMessage());
                Toast.makeText(SearchActivity.this, "search failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpUsersList() {
        adapter = new UserListAdapter(this, users, new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position = (int)view.getTag();
                User user = adapter.getItem(position);

                currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser.getUid().equals(user.getUid())) {
                    //if the user clicked his&her own profile
                    //open the profile activity
                    Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    //view other users' profile
                    Intent intent = new Intent(SearchActivity.this, OthersProfileActivity.class);
                    intent.putExtra("USER_ID", user.getUid());
                    startActivity(intent);
                }
            }
        });
        userResultView.setAdapter(adapter);
    }

    private void getElasticSearchPassword() {
        Log.d(TAG, "getElasticSearchPassword() : retrieving elastic search password");

        Query query = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.elasticsearch))
                .orderByValue();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                elasticSearchPassword = singleSnapshot.getValue().toString();
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
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_search:
//                    Intent intent2 = new Intent(SearchActivity.this, SearchActivity.class);
//                    startActivity(intent2);
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

}
