package com.example.travelapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.adapters.UserChatListAdapter;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.Chat;
import com.example.travelapp.models.AllChatsData;
import com.example.travelapp.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllChatsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserChatListAdapter userChatListAdapter;
    List<AllChatsData> allChatsList;

    List<User> userList;

    List<String> userIdList;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allchats);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().findItem(R.id.nav_AllChats).setChecked(true);


        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.allchatsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allChatsList = new ArrayList<>();

        userList = new ArrayList<>();

        loadNotifications();


    }

    /*
    * load user specific notifications
    * */
    private void loadNotifications() {

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_Notifications);

        final DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_USERS);

        final DatabaseReference databaseReferenceChat = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_Chats);

        userIdList = new ArrayList<>();

        // Query query = databaseReference.orderByChild("to").equalTo(fUser.getUid());

        /*
        * Query fromNotfUserQuery = databaseReference.orderByChild("from").equalTo(fUser.getUid());
        * fromNotfUserQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String from = "" + ds.child("from").getValue();
                    String to = "" + ds.child("to").getValue();

                    Query query1 = databaseReferenceUser.orderByChild("uid").equalTo(to);
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                User user = ds.getValue(User.class);

                                userList.add(user);


                                userChatListAdapter = new UserChatListAdapter(AllChatsActivity.this, userList);
                                recyclerView.setAdapter(userChatListAdapter);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        * */


        databaseReferenceChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userIdList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat= ds.getValue(Chat.class);

                    if(chat.getSender().equals(fUser.getUid())){
                        userIdList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(fUser.getUid())){
                        userIdList.add(chat.getSender());
                    }
                }

                readChats();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readChats() {
        userList = new ArrayList<>();

        final DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_USERS);

        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user= ds.getValue(User.class);

                    for(String id: userIdList){
                        if(user.getUid().equals(id)){
                            if(userList.size() != 0){
                                for(User user1 : userList){
                                    if(!user.getUid().equals(user1.getUid())){
                                        userList.add(user);
                                    }

                                }
                            }else{
                                userList.add(user);
                            }
                        }
                    }
                }

                userChatListAdapter = new UserChatListAdapter(AllChatsActivity.this, userList);
                recyclerView.setAdapter(userChatListAdapter);


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
                    Intent intent = new Intent(AllChatsActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_search:
                    Intent intent2 = new Intent(AllChatsActivity.this, SearchActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_travel_history:
                    Intent intent3 = new Intent(AllChatsActivity.this, TravelHistoryActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.nav_AllChats:
                    Intent intent4 = new Intent(AllChatsActivity.this, AllChatsActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.nav_profile:
                    Intent intent5 = new Intent(AllChatsActivity.this, ProfileActivity.class);
                    startActivity(intent5);
                    break;

            }
            return false;
        }

    };

}
