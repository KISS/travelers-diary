package com.example.travelapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.adapters.ChatAdapter;
import com.example.travelapp.models.Chat;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView recyclerView;
    ImageView userLogo;
    TextView userChatName, userStatusTv;
    EditText messegeEditText;
    ImageButton sentButton;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef;

    ValueEventListener seenListner;
    DatabaseReference userRefForSeen;

    List<Chat> chatList;
    ChatAdapter chatAdapter;

    String userUid;
    String myUid;
    String usersImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.chat_recyclerView);
        userLogo = findViewById(R.id.userLogo);
        messegeEditText = findViewById(R.id.chatEditText);
        sentButton = findViewById(R.id.sentMessage);
        userChatName = findViewById(R.id.userChatName);
        userStatusTv = findViewById(R.id.userOnlineStatus);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        Intent intent = getIntent();
        userUid = intent.getStringExtra("userUid");

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        myUid = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDbRef = firebaseDatabase.getReference("Users");

        getUserDetails();

        setUpSendButtonClickListener();

        readMessages();

        seenMessage();

        configureToolbar();
    }

    private void getUserDetails() {
        Query userQuery = usersDbRef.orderByChild("uid").equalTo(userUid);

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = "" + ds.child("firstName").getValue();
                    usersImage = "" + ds.child("profilePictureUrl").getValue();

                    String onlineStatus = ""+ds.child("onlineStatus").getValue();
                    if (onlineStatus.equals("online")){
                        userStatusTv.setText(onlineStatus);
                    }else {

                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(Long.parseLong(onlineStatus));
                        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
                        userStatusTv.setText("Last seen: "+ dateTime);

                    }
                    userChatName.setText(name);
                    try {
                        Picasso.get().load(usersImage).placeholder(R.drawable.ic_person_black_24dp).into(userLogo);

                    } catch (Exception e) {

                        Picasso.get().load(R.drawable.ic_person_black_24dp).into(userLogo);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpSendButtonClickListener() {
        sentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messegeEditText.getText().toString().trim();

                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(ChatActivity.this, getString(R.string.empty_message_error), Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(message);
                }
            }
        });
    }

    /*
    * Send message
    * */
    private void seenMessage() {

        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListner = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(userUid)) {
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("seen", true);
                        ds.getRef().updateChildren(hasSeenHashMap);


                    }
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages() {

        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    System.out.println(chat+"Hi heloooooooooooooo");
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(userUid) ||
                            chat.getReceiver().equals(userUid) && chat.getSender().equals(myUid))

                    {
                        chatList.add(chat);
                    }

                    chatAdapter = new ChatAdapter(ChatActivity.this, chatList, usersImage);
                    recyclerView.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        myUid = user.getUid();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", userUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("seen", false);


        databaseReference.child("Chats").push().setValue(hashMap);

        messegeEditText.setText("");

    }

    private  void checkOnlineStatus(String status){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);
        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onPause() {

        String timestamp = String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        super.onPause();
        userRefForSeen.removeEventListener(seenListner);

    }

    @Override
    protected void onStart() {
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onResume() {
        checkOnlineStatus("online");
        super.onResume();
    }

    private void configureToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_search:
                    Intent intent2 = new Intent(ChatActivity.this, SearchActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_travel_history:
                    Intent intent3 = new Intent(ChatActivity.this, TravelHistoryActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.nav_AllChats:
                    Intent intent4 = new Intent(ChatActivity.this, AllChatsActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.nav_profile:
                    Intent intent5 = new Intent(ChatActivity.this, ProfileActivity.class);
                    startActivity(intent5);
                    break;

            }
            return false;
        }

    };


}
